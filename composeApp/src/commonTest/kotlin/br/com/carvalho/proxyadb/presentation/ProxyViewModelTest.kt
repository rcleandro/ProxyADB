package br.com.carvalho.proxyadb.presentation

import br.com.carvalho.proxyadb.domain.AdbRepository
import br.com.carvalho.proxyadb.domain.AdbResult
import br.com.carvalho.proxyadb.domain.CheckAdbAvailabilityUseCase
import br.com.carvalho.proxyadb.domain.DisableProxyUseCase
import br.com.carvalho.proxyadb.domain.EnableProxyUseCase
import br.com.carvalho.proxyadb.domain.GetLocalIpUseCase
import br.com.carvalho.proxyadb.domain.NetworkRepository
import br.com.carvalho.proxyadb.domain.ProxyConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProxyViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ProxyViewModel
    
    private var mockIp: String? = "192.168.1.10"
    private var mockAdbAvailable = true
    private var lastResult: AdbResult = AdbResult.Success("OK")

    private val fakeAdbRepo = object : AdbRepository {
        override suspend fun isAvailable(): Boolean = mockAdbAvailable
        override suspend fun setProxy(config: ProxyConfig): AdbResult = lastResult
    }

    private val fakeNetworkRepo = object : NetworkRepository {
        override fun getLocalIpAddress(): String? = mockIp
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel = ProxyViewModel(
            enableProxy = EnableProxyUseCase(fakeAdbRepo),
            disableProxy = DisableProxyUseCase(fakeAdbRepo),
            checkAdb = CheckAdbAvailabilityUseCase(fakeAdbRepo),
            getLocalIp = GetLocalIpUseCase(fakeNetworkRepo)
        )
    }

    @Test
    fun `initial state is correctly loaded`() = runTest {
        mockIp = "192.168.1.10"
        mockAdbAvailable = true
        
        createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("192.168.1.10", state.localIp)
        assertEquals(AdbStatus.Available, state.adbStatus)
        assertFalse(state.proxyEnabled)
    }

    @Test
    fun `toggle proxy updates state on success`() = runTest {
        createViewModel()
        advanceUntilIdle()

        viewModel.onToggleProxy()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.proxyEnabled)
        assertFalse(viewModel.uiState.value.toggleLoading)
    }

    @Test
    fun `port change updates state`() = runTest {
        createViewModel()
        advanceUntilIdle()

        viewModel.onPortChange("9999")
        
        assertEquals("9999", viewModel.uiState.value.port)
    }

    @Test
    fun `port change rejects non-numeric input`() = runTest {
        createViewModel()
        advanceUntilIdle()
        val initialPort = viewModel.uiState.value.port

        viewModel.onPortChange("abcd")
        
        assertEquals(initialPort, viewModel.uiState.value.port)
    }

    @Test
    fun `adb unavailable status is correctly handled`() = runTest {
        mockAdbAvailable = false
        
        createViewModel()
        advanceUntilIdle()

        assertEquals(AdbStatus.Unavailable, viewModel.uiState.value.adbStatus)
        assertTrue(viewModel.uiState.value.userMessage is UserMessage.Error)
    }

    @Test
    fun `proxy fails when ip is missing`() = runTest {
        mockIp = null
        createViewModel()
        advanceUntilIdle()

        viewModel.onToggleProxy()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.proxyEnabled)
        assertTrue(viewModel.uiState.value.userMessage is UserMessage.Error)
    }
}
