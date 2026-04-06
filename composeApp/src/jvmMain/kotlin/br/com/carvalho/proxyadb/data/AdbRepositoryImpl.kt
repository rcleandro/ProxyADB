package br.com.carvalho.proxyadb.data

import br.com.carvalho.proxyadb.core.AppConstants
import br.com.carvalho.proxyadb.domain.AdbRepository
import br.com.carvalho.proxyadb.domain.AdbResult
import br.com.carvalho.proxyadb.domain.FailureReason
import br.com.carvalho.proxyadb.domain.ProxyConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Runs ADB shell commands via [ProcessBuilder].
 *
 * Bug fix: [isAvailable] now calls [Process.waitFor] before reading the exit code.
 * Previously, [Process.exitValue] was called immediately after [Process.start], which
 * throws [IllegalThreadStateException] if the process has not yet terminated — causing
 * ADB to appear unavailable even when correctly installed.
 */
class AdbRepositoryImpl : AdbRepository {

    private val isWindows = System.getProperty(AppConstants.SYS_PROP_OS_NAME)
        .lowercase()
        .contains(AppConstants.OS_NAME_WINDOWS)

    override suspend fun isAvailable(): Boolean =
        runCommand(AppConstants.ADB_COMMAND_VERSION).exitCode == 0

    override suspend fun setProxy(config: ProxyConfig): AdbResult {
        val command = "${AppConstants.ADB_COMMAND_PROXY} ${config.toAdbValue()}"
        val result = runCommand(command)

        return when {
            result.exitCode == 0 ->
                AdbResult.Success(result.output)

            result.error.contains(AppConstants.ADB_ERROR_NO_DEVICES) ->
                AdbResult.Failure(FailureReason.DeviceNotConnected, result.error)

            result.error.contains(AppConstants.ADB_ERROR_NO_FILE) || result.exitCode == -1 ->
                AdbResult.Failure(FailureReason.AdbNotFound, result.error)

            else ->
                AdbResult.Failure(FailureReason.CommandError(result.exitCode), result.error)
        }
    }

    private suspend fun runCommand(command: String): CommandResult = withContext(Dispatchers.IO) {
        runCatching {
            val process = execute(command)
            val output = process.inputStream.bufferedReader().readText().trim()
            val error = process.errorStream.bufferedReader().readText().trim()
            val exitCode = process.waitFor()
            CommandResult(exitCode, output, error)
        }.getOrElse { 
            CommandResult(-1, "", it.message ?: "") 
        }
    }

    private fun execute(command: String): Process {
        val shell = if (isWindows) {
            listOf(AppConstants.ADB_SHELL_WINDOWS, AppConstants.ADB_FLAG_WINDOWS, command)
        } else {
            listOf(AppConstants.ADB_SHELL_UNIX, AppConstants.ADB_FLAG_UNIX, command)
        }
        return ProcessBuilder(shell)
            .redirectErrorStream(false)
            .start()
    }

    private data class CommandResult(
        val exitCode: Int,
        val output: String,
        val error: String
    )
}