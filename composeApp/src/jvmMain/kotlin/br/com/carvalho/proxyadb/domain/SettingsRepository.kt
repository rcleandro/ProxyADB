package br.com.carvalho.proxyadb.domain

interface SettingsRepository {
    fun getLastPort(): Int
    fun saveLastPort(port: Int)
}
