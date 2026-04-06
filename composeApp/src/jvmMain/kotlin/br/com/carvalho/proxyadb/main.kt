package br.com.carvalho.proxyadb

import androidx.compose.runtime.SideEffect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import br.com.carvalho.proxyadb.core.AppConstants
import br.com.carvalho.proxyadb.core.StringKeys
import br.com.carvalho.proxyadb.core.Strings
import br.com.carvalho.proxyadb.di.appModule
import br.com.carvalho.proxyadb.presentation.AppColors
import br.com.carvalho.proxyadb.presentation.ProxyScreen
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinApplication
import proxyadb.composeapp.generated.resources.Res
import proxyadb.composeapp.generated.resources.app_icon
import java.awt.Color as AwtColor

fun main() = application {
    KoinApplication(application = {
        modules(appModule)
    }) {
        val icon = painterResource(Res.drawable.app_icon)
        Window(
            onCloseRequest = ::exitApplication,
            title = Strings[StringKeys.WINDOW_TITLE],
            icon = icon,
            state = WindowState(
                width = AppConstants.WINDOW_WIDTH_DP.dp,
                height = AppConstants.WINDOW_HEIGHT_DP.dp,
            ),
            resizable = false,
        ) {
            SideEffect {
                val color = AppColors.background
                val awtColor = AwtColor(color.red, color.green, color.blue, color.alpha)
                window.background = awtColor
                
                val os = System.getProperty(AppConstants.SYS_PROP_OS_NAME).lowercase()
                if (os.contains(AppConstants.OS_NAME_MAC)) {
                    window.rootPane.putClientProperty(AppConstants.MAC_PROP_FULL_WINDOW_CONTENT, true)
                    window.rootPane.putClientProperty(AppConstants.MAC_PROP_TRANSPARENT_TITLE_BAR, true)
                    window.rootPane.putClientProperty(AppConstants.MAC_PROP_WINDOW_TITLE_VISIBLE, false)
                }
            }
            ProxyScreen()
        }
    }
}
