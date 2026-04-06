package br.com.carvalho.proxyadb

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import br.com.carvalho.proxyadb.presentation.components.CustomTitleBar
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinApplication
import proxyadb.composeapp.generated.resources.Res
import proxyadb.composeapp.generated.resources.app_icon
import java.awt.Color as AwtColor

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
                width = AppConstants.WINDOW_WIDTH_DP.dp,
                height = AppConstants.WINDOW_HEIGHT_DP.dp,
            ),
            resizable = false,
            undecorated = !isMac,
            transparent = true,
        ) {
            SideEffect {
                window.background = AwtColor(
                    AppConstants.COLOR_RGB_BLACK,
                    AppConstants.COLOR_RGB_BLACK,
                    AppConstants.COLOR_RGB_BLACK,
                    AppConstants.COLOR_ALPHA_TRANSPARENT
                )
                
                if (isMac) {
                    window.rootPane.putClientProperty(AppConstants.MAC_PROP_FULL_WINDOW_CONTENT, true)
                    window.rootPane.putClientProperty(AppConstants.MAC_PROP_TRANSPARENT_TITLE_BAR, true)
                    window.rootPane.putClientProperty(AppConstants.MAC_PROP_WINDOW_TITLE_VISIBLE, false)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(AppConstants.SIZE_CORNER_RADIUS_MEDIUM.dp))
                    .background(AppColors.background)
                    .border(
                        AppConstants.SIZE_BORDER_THICKNESS.dp,
                        AppColors.surfaceBorder,
                        RoundedCornerShape(AppConstants.SIZE_CORNER_RADIUS_MEDIUM.dp)
                    )
            ) {
                Column {
                    if (!isMac) {
                        CustomTitleBar(
                            onClose = ::exitApplication,
                            onMinimize = { window.state = java.awt.Frame.ICONIFIED }
                        )
                    }
                    
                    ProxyScreen()
                }
            }
        }
    }
}
