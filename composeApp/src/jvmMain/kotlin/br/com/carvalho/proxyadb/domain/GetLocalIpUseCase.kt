package br.com.carvalho.proxyadb.domain

/**
 * Resolves the best local IP address to use as the proxy host.
 */
class GetLocalIpUseCase(private val networkRepository: NetworkRepository) {
    operator fun invoke(): String? = networkRepository.getLocalIpAddress()
}