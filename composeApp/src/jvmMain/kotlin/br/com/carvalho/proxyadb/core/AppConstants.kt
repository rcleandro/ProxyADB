package br.com.carvalho.proxyadb.core

/**
 * Application-wide business and system constants.
 */
object AppConstants {
    const val ADB_COMMAND_VERSION = "adb version"
    const val ADB_COMMAND_PROXY = "adb shell settings put global http_proxy"
    
    const val ADB_SHELL_WINDOWS = "cmd.exe"
    const val ADB_SHELL_UNIX = "sh"
    const val ADB_FLAG_WINDOWS = "/c"
    const val ADB_FLAG_UNIX = "-c"
    
    const val ADB_ERROR_NO_DEVICES = "no devices"
    const val ADB_ERROR_NO_FILE = "No such file"
    
    const val IP_RANGE_192 = "192.168."
    const val IP_RANGE_10 = "10."
    const val IP_RANGE_172 = "172."
    
    const val PROXY_DEFAULT_PORT = 8888
    const val PROXY_DISABLED_VALUE = ":0"
    const val PROXY_PORT_MAX_CHARS = 5
    
    const val NETWORK_PREFIX_WLAN = "wlan"
    const val NETWORK_PREFIX_EN0 = "en0"
    const val NETWORK_PREFIX_EN1 = "en1"
    const val NETWORK_PREFIX_ETH = "eth"
    const val NETWORK_PREFIX_WIFI = "wi-fi"
    const val NETWORK_PREFIX_WIRELESS = "wireless"
    
    const val EXCLUDE_DOCKER = "docker"
    const val EXCLUDE_VMWARE = "vmware"
    const val EXCLUDE_VBOX = "vbox"
    const val EXCLUDE_VIRTUAL = "virtual"
    const val EXCLUDE_LOOPBACK = "loopback"
    
    const val SYS_PROP_OS_NAME = "os.name"
    const val OS_NAME_MAC = "mac"
    const val OS_NAME_WINDOWS = "windows"

    const val MAC_PROP_FULL_WINDOW_CONTENT = "apple.awt.fullWindowContent"
    const val MAC_PROP_TRANSPARENT_TITLE_BAR = "apple.awt.transparentTitleBar"
    const val MAC_PROP_WINDOW_TITLE_VISIBLE = "apple.awt.windowTitleVisible"
}
