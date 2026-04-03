package br.com.carvalho.proxyadb.core

import br.com.carvalho.proxyadb.core.Strings.format
import br.com.carvalho.proxyadb.core.Strings.get
import java.util.Locale
import java.util.Properties

/**
 * All string resource keys as [const val].
 * The UI and ViewModel reference these instead of raw strings.
 */
object StringKeys {
    const val APP_TITLE = "app_title"
    const val APP_SUBTITLE = "app_subtitle"
    const val WINDOW_TITLE = "window_title"

    const val LABEL_DETECTED_IP = "label_detected_ip"
    const val LABEL_PORT = "label_port"
    const val LABEL_ADB_PATH = "label_adb_path"

    const val ADB_STATUS_CHECKING = "adb_status_checking"
    const val ADB_STATUS_AVAILABLE = "adb_status_available"
    const val ADB_STATUS_UNAVAILABLE = "adb_status_unavailable"

    const val IP_NOT_DETECTED = "ip_not_detected"

    const val TOGGLE_LOADING = "toggle_loading"
    const val TOGGLE_ACTIVE = "toggle_active"
    const val TOGGLE_INACTIVE = "toggle_inactive"

    const val CMD_PREVIEW_LABEL_ENABLE = "cmd_preview_label_enable"
    const val CMD_PREVIEW_LABEL_DISABLE = "cmd_preview_label_disable"

    const val MSG_PROXY_ENABLED = "msg_proxy_enabled"
    const val MSG_PROXY_DISABLED = "msg_proxy_disabled"
    const val MSG_ADB_NOT_FOUND = "msg_adb_not_found"
    const val MSG_IP_NOT_DETECTED = "msg_ip_not_detected"

    const val ERR_ADB_NOT_FOUND = "err_adb_not_found"
    const val ERR_DEVICE_NOT_CONNECTED = "err_device_not_connected"
    const val ERR_COMMAND_ERROR = "err_command_error"
    const val ERR_UNEXPECTED = "err_unexpected"
    const val ERR_IP_MISSING = "err_ip_missing"
}

/**
 * Loads the correct `.properties` file for the system locale and exposes
 * [get] / [format] helpers.
 *
 * Resolution order: exact locale match → language-only match → English fallback.
 */
object Strings {

    private val props: Properties = loadProperties()

    operator fun get(key: String): String =
        props.getProperty(key) ?: "[$key]"

    /** Replaces %d / %s placeholders using [String.format]. */
    fun format(key: String, vararg args: Any): String =
        String.format(get(key), *args)

    private fun loadProperties(): Properties {
        val locale = Locale.getDefault()

        val candidates = listOf(
            "i18n/strings_${locale.language}_${locale.country}.properties",
            "i18n/strings_${locale.language}.properties",
            "i18n/strings_en.properties",
        )

        for (path in candidates) {
            val stream = Strings::class.java.classLoader?.getResourceAsStream(path)
            if (stream != null) {
                return Properties().apply { load(stream.reader(Charsets.UTF_8)) }
            }
        }

        return Properties()
    }
}