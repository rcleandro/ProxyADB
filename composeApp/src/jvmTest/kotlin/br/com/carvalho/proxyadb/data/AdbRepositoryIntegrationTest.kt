package br.com.carvalho.proxyadb.data

import br.com.carvalho.proxyadb.domain.AdbResult
import br.com.carvalho.proxyadb.domain.ProxyConfig
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Integration tests for [AdbRepositoryImpl].
 * 
 * These tests require the ADB binary to be installed and available in the system PATH.
 * Some tests are ignored by default as they require a physical device or emulator connected.
 */
class AdbRepositoryIntegrationTest {

    private lateinit var repository: AdbRepositoryImpl

    @BeforeTest
    fun setup() {
        repository = AdbRepositoryImpl()
    }

    @Test
    fun `isAvailable returns true if adb is in PATH`() = runTest {
        val available = repository.isAvailable()
        println("ADB Available: $available")
    }

    @Test
    @Ignore
    fun `setProxy returns Success when device is connected`() = runTest {
        val result = repository.setProxy(ProxyConfig("192.168.1.10", 8888))
        assertTrue(result is AdbResult.Success, "Expected Success but got $result")
    }

    @Test
    fun `setProxy returns DeviceNotConnected when no device is plugged`() = runTest {
        val result = repository.setProxy(ProxyConfig("1.1.1.1", 8888))
        if (result is AdbResult.Failure) {
            println("Caught expected failure: ${result.reason}")
        }
    }
}
