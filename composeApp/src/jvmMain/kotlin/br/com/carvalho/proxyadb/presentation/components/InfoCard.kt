package br.com.carvalho.proxyadb.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import br.com.carvalho.proxyadb.core.StringKeys
import br.com.carvalho.proxyadb.core.Strings
import br.com.carvalho.proxyadb.presentation.AdbStatus
import br.com.carvalho.proxyadb.presentation.AppColors
import br.com.carvalho.proxyadb.presentation.Dimens
import br.com.carvalho.proxyadb.presentation.ProxyUiState

@Composable
fun InfoCard(
    state: ProxyUiState,
    onIpChange: (String) -> Unit,
    onPortChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clip(RoundedCornerShape(Dimens.CornerRadiusMedium))
            .background(AppColors.surface)
            .border(
                Dimens.SizeBorderThickness,
                AppColors.surfaceBorder,
                RoundedCornerShape(Dimens.CornerRadiusMedium)
            )
            .padding(Dimens.PaddingXXLarge),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingZero),
    ) {
        InfoRow(label = Strings[StringKeys.LABEL_DETECTED_IP]) {
            BasicTextField(
                value = state.localIp,
                onValueChange = onIpChange,
                singleLine = true,
                cursorBrush = SolidColor(AppColors.mono),
                textStyle = TextStyle(
                    fontSize = Dimens.FontSizeMedium,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    color = if (state.localIp.isNotBlank()) AppColors.mono else AppColors.error,
                    textAlign = TextAlign.End
                ),
                modifier = Modifier.width(Dimens.SizeIpInputWidth),
            )
        }
        HorizontalDivider()
        InfoRow(label = Strings[StringKeys.LABEL_PORT]) {
            BasicTextField(
                value = state.port,
                onValueChange = onPortChange,
                singleLine = true,
                cursorBrush = SolidColor(AppColors.mono),
                textStyle = TextStyle(
                    fontSize = Dimens.FontSizeMedium,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.mono,
                    textAlign = TextAlign.End
                ),
                modifier = Modifier.width(Dimens.SizePortWidth),
            )
        }
        HorizontalDivider()
        InfoRow(label = Strings[StringKeys.LABEL_ADB_PATH]) {
            val (label, color) = when (state.adbStatus) {
                AdbStatus.Checking -> Strings[StringKeys.ADB_STATUS_CHECKING] to AppColors.textSecondary
                AdbStatus.Available -> Strings[StringKeys.ADB_STATUS_AVAILABLE] to AppColors.mono
                AdbStatus.Unavailable -> Strings[StringKeys.ADB_STATUS_UNAVAILABLE] to AppColors.error
            }
            Text(
                text = label,
                fontSize = Dimens.FontSizeMedium,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
                color = color
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.PaddingMediumV),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontSize = Dimens.FontSizeSmall,
            color = AppColors.textSecondary,
            letterSpacing = Dimens.LetterSpacingXSmall
        )
        content()
    }
}

@Composable
private fun HorizontalDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.SizeDividerHeight)
            .background(AppColors.surfaceBorder)
    )
}
