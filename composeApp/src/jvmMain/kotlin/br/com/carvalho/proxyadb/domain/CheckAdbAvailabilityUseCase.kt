package br.com.carvalho.proxyadb.domain

/**
 * Checks whether the ADB binary is reachable on the host machine's PATH.
 */
class CheckAdbAvailabilityUseCase(private val adbRepository: AdbRepository) {
    suspend operator fun invoke(): Boolean =
        adbRepository.isAvailable()
}