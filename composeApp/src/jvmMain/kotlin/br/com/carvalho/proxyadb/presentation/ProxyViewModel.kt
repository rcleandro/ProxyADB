package br.com.carvalho.proxyadb.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.carvalho.proxyadb.core.AppConstants
import br.com.carvalho.proxyadb.core.StringKeys
import br.com.carvalho.proxyadb.core.Strings
import br.com.carvalho.proxyadb.domain.AdbResult
import br.com.carvalho.proxyadb.domain.CheckAdbAvailabilityUseCase
import br.com.carvalho.proxyadb.domain.DisableProxyUseCase
import br.com.carvalho.proxyadb.domain.EnableProxyUseCase
import br.com.carvalho.proxyadb.domain.FailureReason
import br.com.carvalho.proxyadb.domain.GetLocalIpUseCase
import br.com.carvalho.proxyadb.domain.ProxyConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Immutable snapshot of everything the UI needs to render.
 * Compose re-composes only the parts that changed.
 */
data class ProxyUiState(
    val localIp: String? = null,
    val port: String = AppConstants.PROXY_DEFAULT_PORT.toString(),
    val proxyEnabled: Boolean = false,
    val adbStatus: AdbStatus = AdbStatus.Checking,
    val toggleLoading: Boolean = false,
    val userMessage: UserMessage? = null,
)

sealed interface AdbStatus {
    data object Checking : AdbStatus
    data object Available : AdbStatus
    data object Unavailable : AdbStatus
}

sealed interface UserMessage {
    data class Success(val text: String) : UserMessage
    data class Error(val text: String) : UserMessage
}

/**
 * Survives recomposition and drives the UI via [uiState].
 *
 * Business logic lives in use cases; this class only orchestrates them
 * and maps results into [ProxyUiState]. All user-visible strings are
 * resolved via [Strings] so the ViewModel stays locale-aware without
 * coupling to the UI layer.
 */
class ProxyViewModel(
    private val enableProxy: EnableProxyUseCase,
    private val disableProxy: DisableProxyUseCase,
    private val checkAdb: CheckAdbAvailabilityUseCase,
    private val getLocalIp: GetLocalIpUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProxyUiState())
    val uiState: StateFlow<ProxyUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    fun onToggleProxy() {
        val state = _uiState.value
        if (state.toggleLoading || state.adbStatus != AdbStatus.Available) return

        viewModelScope.launch {
            _uiState.update { it.copy(toggleLoading = true, userMessage = null) }

            val result = if (state.proxyEnabled) {
                disableProxy()
            } else {
                val ip =
                    state.localIp ?: return@launch emitError(Strings[StringKeys.ERR_IP_MISSING])
                val port = state.port.toIntOrNull() ?: AppConstants.PROXY_DEFAULT_PORT
                enableProxy(ProxyConfig(host = ip, port = port))
            }

            _uiState.update { current ->
                when (result) {
                    is AdbResult.Success -> current.copy(
                        proxyEnabled = !state.proxyEnabled,
                        toggleLoading = false,
                        userMessage = UserMessage.Success(
                            if (!state.proxyEnabled) Strings[StringKeys.MSG_PROXY_ENABLED]
                            else Strings[StringKeys.MSG_PROXY_DISABLED]
                        )
                    )

                    is AdbResult.Failure -> current.copy(
                        toggleLoading = false,
                        userMessage = UserMessage.Error(result.toReadableMessage())
                    )
                }
            }
        }
    }

    fun onPortChange(newPort: String) {
        if (newPort.all { it.isDigit() } && newPort.length <= AppConstants.PROXY_PORT_MAX_CHARS) {
            _uiState.update { it.copy(port = newPort) }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val ip = getLocalIp()
            val available = checkAdb()

            _uiState.update {
                it.copy(
                    localIp = ip,
                    adbStatus = if (available) AdbStatus.Available else AdbStatus.Unavailable,
                    userMessage = when {
                        !available -> UserMessage.Error(Strings[StringKeys.MSG_ADB_NOT_FOUND])
                        ip == null -> UserMessage.Error(Strings[StringKeys.MSG_IP_NOT_DETECTED])
                        else -> null
                    }
                )
            }
        }
    }

    private fun emitError(msg: String) {
        _uiState.update { it.copy(toggleLoading = false, userMessage = UserMessage.Error(msg)) }
    }

    private fun AdbResult.Failure.toReadableMessage(): String = when (reason) {
        FailureReason.AdbNotFound -> Strings[StringKeys.ERR_ADB_NOT_FOUND]
        FailureReason.DeviceNotConnected -> Strings[StringKeys.ERR_DEVICE_NOT_CONNECTED]
        is FailureReason.CommandError -> Strings.format(
            StringKeys.ERR_COMMAND_ERROR,
            reason.exitCode,
            details
        )

        is FailureReason.UnexpectedException -> Strings.format(
            StringKeys.ERR_UNEXPECTED,
            reason.message
        )
    }
}