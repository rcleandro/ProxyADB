package br.com.carvalho.proxyadb.domain

/**
 * Disables the HTTP proxy on the connected Android device.
 */
class DisableProxyUseCase(private val adbRepository: AdbRepository) {
    suspend operator fun invoke(): AdbResult =
        adbRepository.setProxy(ProxyConfig.DISABLED)
}