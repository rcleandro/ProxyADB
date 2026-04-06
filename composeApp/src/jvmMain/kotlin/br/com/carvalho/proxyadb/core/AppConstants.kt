package br.com.carvalho.proxyadb.core

/**
 * Application-wide constants.
 * All magic numbers and fixed strings live here as [const val].
 */
object AppConstants {
    const val WINDOW_WIDTH_DP = 400
    const val WINDOW_HEIGHT_DP = 680
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
    const val TOGGLE_ANIM_DURATION_MS = 400
    const val TOGGLE_THUMB_ANIM_DURATION_MS = 300
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
    
    const val COLOR_ALPHA_TRANSPARENT = 0
    const val COLOR_RGB_BLACK = 0
    
    const val TITLE_BAR_BUTTON_ICON_SIZE = 14
    const val PADDING_MEDIUM_V = 12
    const val PADDING_LARGE = 14
    const val PADDING_XXLARGE = 20
    const val PADDING_SCREEN = 32
    const val SPACING_ZERO = 0
    const val SPACING_SMALL = 6
    const val SPACING_MEDIUM = 10
    const val SPACING_LARGE = 24
    const val SIZE_TOGGLE_WIDTH = 72
    const val SIZE_TOGGLE_HEIGHT = 40
    const val SIZE_TOGGLE_THUMB = 36
    const val SIZE_TOGGLE_THUMB_OFFSET_START = 2
    const val SIZE_TOGGLE_THUMB_OFFSET_END = 34
    const val SIZE_DIVIDER_HEIGHT = 0.5
    const val SIZE_PORT_WIDTH = 56
    const val SIZE_IP_INPUT_WIDTH = 150
    const val SIZE_BORDER_THICKNESS = 1.0
    const val SIZE_BORDER_THICK_THICKNESS = 2.0
    const val SIZE_CORNER_RADIUS_SMALL = 12
    const val SIZE_CORNER_RADIUS_MEDIUM = 16
    const val SIZE_CORNER_RADIUS_LARGE = 20
    const val SIZE_MAC_TOP_PADDING = 24
    const val SIZE_TITLE_BAR_HEIGHT = 40
    const val SIZE_TITLE_BAR_BUTTON = 40
    const val SIZE_TITLE_BAR_ICON = 18
    const val FONT_SIZE_XSMALL = 9
    const val FONT_SIZE_SMALL = 11
    const val FONT_SIZE_MEDIUM = 12
    const val FONT_SIZE_LARGE = 24
    const val LETTER_SPACING_XSMALL = 0.5
    const val LETTER_SPACING_SMALL = 1.0
    const val LETTER_SPACING_LARGE = 2.0
    const val LINE_HEIGHT_SMALL = 17
}