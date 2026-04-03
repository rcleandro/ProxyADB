package br.com.carvalho.proxyadb.domain

/**
 * Represents the proxy configuration to be applied to the Android device.
 */
data class ProxyConfig(
    val host: String,
    val port: Int = DEFAULT_PORT
) {
    companion object {
        const val DEFAULT_PORT = 8888
        val DISABLED = ProxyConfig(host = "", port = 0)
    }

    val isEnabled: Boolean get() = host.isNotBlank() && port != 0

    /** Returns the value passed to the ADB settings command. */
    fun toAdbValue(): String = if (isEnabled) "$host:$port" else ":0"
}

/**
 * Represents the result of an ADB command execution.
 */
sealed interface AdbResult {
    data class Success(val output: String) : AdbResult
    data class Failure(val reason: FailureReason, val details: String = "") : AdbResult
}

sealed interface FailureReason {
    data object AdbNotFound : FailureReason
    data object DeviceNotConnected : FailureReason
    data class CommandError(val exitCode: Int) : FailureReason
    data class UnexpectedException(val message: String) : FailureReason
}

/**
 * Provides access to ADB commands. Abstracted so it can be faked in tests.
 */
interface AdbRepository {
    suspend fun isAvailable(): Boolean
    suspend fun setProxy(config: ProxyConfig): AdbResult
}

/**
 * Provides network information for the local machine.
 */
interface NetworkRepository {
    fun getLocalIpAddress(): String?
}