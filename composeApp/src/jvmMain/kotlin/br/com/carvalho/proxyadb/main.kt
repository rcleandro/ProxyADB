package br.com.carvalho.proxyadb

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import br.com.carvalho.proxyadb.core.AppConstants
import br.com.carvalho.proxyadb.core.StringKeys
import br.com.carvalho.proxyadb.core.Strings
import br.com.carvalho.proxyadb.di.appModule
import br.com.carvalho.proxyadb.presentation.Dimens
import br.com.carvalho.proxyadb.presentation.ProxyScreen
import br.com.carvalho.proxyadb.presentation.components.CustomTitleBar
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinApplication
import proxyadb.composeapp.generated.resources.Res
import proxyadb.composeapp.generated.resources.app_icon

fun main() = application {
    KoinApplication(application = {
        modules(appModule)
    }) {
        val os = remember { System.getProperty(AppConstants.SYS_PROP_OS_NAME).lowercase() }
        val isMac = os.contains(AppConstants.OS_NAME_MAC)
        
        val icon = painterResource(Res.drawable.app_icon)
        Window(
            onCloseRequest = ::exitApplication,
            title = Strings[StringKeys.WINDOW_TITLE],
            icon = icon,
            state = WindowState(
                width = Dimens.WindowWidth,
                height = Dimens.WindowHeight,
            ),
            resizable = false,
            undecorated = !isMac,
            transparent = !isMac,
        ) {
            CustomTitleBar(
                isMac = isMac,
                onClose = ::exitApplication,
                onMinimize = { window.state = java.awt.Frame.ICONIFIED }
            ) {
                ProxyScreen()
            }
        }
    }
}
