package br.com.carvalho.proxyadb.data

import br.com.carvalho.proxyadb.core.AppConstants
import br.com.carvalho.proxyadb.domain.NetworkRepository
import java.net.Inet4Address
import java.net.NetworkInterface

/**
 * Resolves the host machine's LAN IP address by inspecting active network interfaces.
 *
 * Priority order:
 * 1. Interfaces whose name starts with a preferred prefix (wlan, en, eth, wi-fi…)
 * 2. Any other non-loopback, non-virtual IPv4 address in a private range
 */
class NetworkRepositoryImpl : NetworkRepository {

    private val preferredPrefixes = listOf(
        AppConstants.NETWORK_PREFIX_WLAN,
        AppConstants.NETWORK_PREFIX_EN0,
        AppConstants.NETWORK_PREFIX_EN1,
        AppConstants.NETWORK_PREFIX_ETH,
        AppConstants.NETWORK_PREFIX_WIFI,
        AppConstants.NETWORK_PREFIX_WIRELESS
    )
    private val excludedKeywords = listOf(
        AppConstants.EXCLUDE_DOCKER,
        AppConstants.EXCLUDE_VMWARE,
        AppConstants.EXCLUDE_VBOX,
        AppConstants.EXCLUDE_VIRTUAL,
        AppConstants.EXCLUDE_LOOPBACK
    )

    override fun getLocalIpAddress(): String? {
        val candidates = collectCandidates()
        return preferredPrefixes
            .firstNotNullOfOrNull { prefix ->
                candidates.firstOrNull { (name, _) ->
                    name.startsWith(prefix = prefix)
                }?.second
            }
            ?: candidates.firstOrNull()?.second
    }

    private fun collectCandidates(): List<Pair<String, String>> =
        NetworkInterface.getNetworkInterfaces()
            ?.toList()
            .orEmpty()
            .filter { iface -> iface.isUp && !iface.isLoopback && !iface.isVirtual && iface.isNotExcluded() }
            .flatMap { iface ->
                iface.inetAddresses
                    .toList()
                    .filterIsInstance<Inet4Address>()
                    .filter { it.isPrivateRange() }
                    .map { iface.name.lowercase() to it.hostAddress }
            }

    private fun NetworkInterface.isNotExcluded(): Boolean =
        excludedKeywords.none {
            displayName.lowercase().contains(it) || name.lowercase().contains(it)
        }

    private fun Inet4Address.isPrivateRange(): Boolean {
        val ip = hostAddress ?: return false
        return ip.startsWith(AppConstants.IP_RANGE_192) ||
                ip.startsWith(AppConstants.IP_RANGE_10) ||
                ip.startsWith(AppConstants.IP_RANGE_172)
    }
}