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

    override suspend fun isAvailable(): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val process = execute(AppConstants.ADB_COMMAND_VERSION)
            process.waitFor() == 0
        }.getOrDefault(false)
    }

    override suspend fun setProxy(config: ProxyConfig): AdbResult = withContext(Dispatchers.IO) {
        val command = "${AppConstants.ADB_COMMAND_PROXY} ${config.toAdbValue()}"
        runCatching {
            val process = execute(command)
            val output = process.inputStream.bufferedReader().readText().trim()
            val error = process.errorStream.bufferedReader().readText().trim()
            val code = process.waitFor()

            when {
                code == 0 ->
                    AdbResult.Success(output)

                error.contains(AppConstants.ADB_ERROR_NO_DEVICES) ->
                    AdbResult.Failure(FailureReason.DeviceNotConnected, error)

                else ->
                    AdbResult.Failure(FailureReason.CommandError(code), error)
            }
        }.getOrElse { exception ->
            val reason = if (
                exception is java.io.IOException &&
                exception.message?.contains(AppConstants.ADB_ERROR_NO_FILE) == true
            ) {
                FailureReason.AdbNotFound
            } else {
                FailureReason.UnexpectedException(exception.message.orEmpty())
            }
            AdbResult.Failure(reason)
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
}