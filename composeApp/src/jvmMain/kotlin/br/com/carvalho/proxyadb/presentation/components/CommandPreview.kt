package br.com.carvalho.proxyadb.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import br.com.carvalho.proxyadb.core.AppConstants
import br.com.carvalho.proxyadb.core.StringKeys
import br.com.carvalho.proxyadb.core.Strings
import br.com.carvalho.proxyadb.presentation.AppColors
import br.com.carvalho.proxyadb.presentation.Dimens
import br.com.carvalho.proxyadb.presentation.ProxyUiState

@Composable
fun CommandPreview(state: ProxyUiState) {
    val nextCommand = if (state.proxyEnabled) {
        "${AppConstants.ADB_COMMAND_PROXY} ${AppConstants.PROXY_DISABLED_VALUE}"
    } else {
        val ip = state.localIp.ifBlank { "<IP>" }
        val port = state.port.ifEmpty { AppConstants.PROXY_DEFAULT_PORT.toString() }
        "${AppConstants.ADB_COMMAND_PROXY} $ip:$port"
    }
    val nextLabel = if (state.proxyEnabled) {
        Strings[StringKeys.CMD_PREVIEW_LABEL_DISABLE]
    } else {
        Strings[StringKeys.CMD_PREVIEW_LABEL_ENABLE]
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.CornerRadiusSmall))
            .background(AppColors.surfaceDark)
            .border(
                Dimens.SizeBorderThickness,
                AppColors.surfaceBorder,
                RoundedCornerShape(Dimens.CornerRadiusSmall)
            )
            .padding(Dimens.PaddingLarge),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
    ) {
        Text(
            text = nextLabel,
            fontSize = Dimens.FontSizeXSmall,
            color = AppColors.textSecondary,
            letterSpacing = Dimens.LetterSpacingSmall
        )
        Text(
            text = nextCommand,
            fontSize = Dimens.FontSizeSmall,
            fontFamily = FontFamily.Monospace,
            color = AppColors.mono,
            lineHeight = Dimens.LineHeightSmall
        )
    }
}
