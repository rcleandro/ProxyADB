package br.com.carvalho.proxyadb

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import br.com.carvalho.proxyadb.core.AppConstants
import br.com.carvalho.proxyadb.core.StringKeys
import br.com.carvalho.proxyadb.core.Strings
import br.com.carvalho.proxyadb.di.appModule
import br.com.carvalho.proxyadb.presentation.ProxyScreen
import org.koin.compose.KoinApplication

fun main() = application {
    KoinApplication(application = {
        modules(appModule)
    }) {
        Window(
            onCloseRequest = ::exitApplication,
            title = Strings[StringKeys.WINDOW_TITLE],
            state = WindowState(
                width = AppConstants.WINDOW_WIDTH_DP.dp,
                height = AppConstants.WINDOW_HEIGHT_DP.dp,
            ),
            resizable = false,
        ) {
            ProxyScreen()
        }
    }
}
