package br.com.carvalho.proxyadb.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.FrameWindowScope
import br.com.carvalho.proxyadb.core.AppConstants
import br.com.carvalho.proxyadb.core.StringKeys
import br.com.carvalho.proxyadb.core.Strings
import br.com.carvalho.proxyadb.presentation.AppColors
import org.jetbrains.compose.resources.painterResource
import proxyadb.composeapp.generated.resources.Res
import proxyadb.composeapp.generated.resources.app_icon
import java.awt.Color as AwtColor

@Composable
fun FrameWindowScope.CustomTitleBar(
    isMac: Boolean,
    onClose: () -> Unit,
    onMinimize: () -> Unit,
    content: @Composable () -> Unit
) {
    SideEffect {
        if (isMac) {
            window.rootPane.putClientProperty(
                AppConstants.MAC_PROP_FULL_WINDOW_CONTENT,
                true
            )
            window.rootPane.putClientProperty(
                AppConstants.MAC_PROP_TRANSPARENT_TITLE_BAR,
                true
            )
            window.rootPane.putClientProperty(
                AppConstants.MAC_PROP_WINDOW_TITLE_VISIBLE,
                false
            )
        } else {
            window.background = AwtColor(
                AppConstants.COLOR_RGB_BLACK,
                AppConstants.COLOR_RGB_BLACK,
                AppConstants.COLOR_RGB_BLACK,
                AppConstants.COLOR_ALPHA_TRANSPARENT
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (!isMac) {
                    Modifier
                        .clip(RoundedCornerShape(AppConstants.SIZE_CORNER_RADIUS_MEDIUM.dp))
                        .border(
                            AppConstants.SIZE_BORDER_THICKNESS.dp,
                            AppColors.surfaceBorder,
                            RoundedCornerShape(AppConstants.SIZE_CORNER_RADIUS_MEDIUM.dp)
                        )
                } else Modifier
            )
            .background(AppColors.background)
    ) {
        Column {
            if (isMac) {
                WindowDraggableArea {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(AppConstants.SIZE_MAC_TOP_PADDING.dp)
                    )
                }
            } else {
                WindowDraggableArea {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(AppConstants.SIZE_TITLE_BAR_HEIGHT.dp)
                            .background(AppColors.background)
                            .padding(start = AppConstants.SPACING_MEDIUM.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AppConstants.SPACING_SMALL.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.app_icon),
                                contentDescription = null,
                                modifier = Modifier.size(AppConstants.SIZE_TITLE_BAR_ICON.dp),
                                tint = Color.Unspecified
                            )
                            Text(
                                text = Strings[StringKeys.WINDOW_TITLE],
                                color = AppColors.textSecondary,
                                fontSize = AppConstants.FONT_SIZE_SMALL.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TitleBarButton(
                                onClick = onMinimize,
                                icon = Icons.Default.Minimize,
                                hoverColor = AppColors.surface
                            )
                            TitleBarButton(
                                onClick = onClose,
                                icon = Icons.Default.Close,
                                hoverColor = AppColors.close
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }
    }
}

@Composable
private fun TitleBarButton(
    onClick: () -> Unit,
    icon: ImageVector,
    hoverColor: Color
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier = Modifier
            .size(AppConstants.SIZE_TITLE_BAR_BUTTON.dp)
            .background(if (isHovered) hoverColor else Color.Transparent)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(AppConstants.TITLE_BAR_BUTTON_ICON_SIZE.dp),
            tint = if (isHovered) Color.White else AppColors.textSecondary
        )
    }
}
