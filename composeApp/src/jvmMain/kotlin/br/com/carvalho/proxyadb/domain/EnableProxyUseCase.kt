package br.com.carvalho.proxyadb.domain

/**
 * Enables the HTTP proxy on the connected Android device.
 *
 * Single-responsibility use case that delegates to [AdbRepository].
 * Returns [AdbResult] so the caller decides how to handle success/failure.
 */
class EnableProxyUseCase(private val adbRepository: AdbRepository) {
    suspend operator fun invoke(config: ProxyConfig): AdbResult =
        adbRepository.setProxy(config)
}





