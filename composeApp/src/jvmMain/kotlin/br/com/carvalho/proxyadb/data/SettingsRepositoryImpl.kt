package br.com.carvalho.proxyadb.data

import br.com.carvalho.proxyadb.core.AppConstants
import br.com.carvalho.proxyadb.domain.SettingsRepository
import java.util.prefs.Preferences

class SettingsRepositoryImpl : SettingsRepository {
    private val prefs = Preferences.userNodeForPackage(SettingsRepositoryImpl::class.java)

    override fun getLastPort(): Int {
        return prefs.getInt(KEY_LAST_PORT, AppConstants.PROXY_DEFAULT_PORT)
    }

    override fun saveLastPort(port: Int) {
        prefs.putInt(KEY_LAST_PORT, port)
    }

    companion object {
        private const val KEY_LAST_PORT = "last_port"
    }
}
