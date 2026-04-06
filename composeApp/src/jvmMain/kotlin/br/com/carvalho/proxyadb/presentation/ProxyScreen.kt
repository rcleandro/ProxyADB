package br.com.carvalho.proxyadb.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.carvalho.proxyadb.core.AppConstants
import br.com.carvalho.proxyadb.core.StringKeys
import br.com.carvalho.proxyadb.core.Strings
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
fun ProxyScreen(
    viewModel: ProxyViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    ProxyADBTheme {
        ProxyScreenContent(
            state = state,
            onToggle = viewModel::onToggleProxy,
            onIpChange = viewModel::onIpChange,
            onPortChange = viewModel::onPortChange,
        )
    }
}

@Composable
private fun ProxyScreenContent(
    state: ProxyUiState,
    onToggle: () -> Unit,
    onIpChange: (String) -> Unit,
    onPortChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.background)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { focusManager.clearFocus() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppConstants.SPACING_LARGE.dp),
        contentPadding = PaddingValues(AppConstants.PADDING_SCREEN.dp),
    ) {
        item { AppTitle() }
        item { InfoCard(state = state, onIpChange = onIpChange, onPortChange = onPortChange) }
        item { ToggleSection(state = state, onToggle = onToggle) }
        item { CommandPreview(state = state) }
        item { StatusMessage(message = state.userMessage) }
    }
}

@Composable
private fun AppTitle() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = Strings[StringKeys.APP_TITLE],
            fontSize = AppConstants.FONT_SIZE_LARGE.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.textPrimary,
            letterSpacing = AppConstants.LETTER_SPACING_SMALL.sp,
        )
        Text(
            text = Strings[StringKeys.APP_SUBTITLE],
            fontSize = AppConstants.FONT_SIZE_MEDIUM.sp,
            color = AppColors.textSecondary,
            letterSpacing = AppConstants.LETTER_SPACING_XSMALL.sp,
        )
    }
}

@Composable
private fun InfoCard(
    state: ProxyUiState,
    onIpChange: (String) -> Unit,
    onPortChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clip(RoundedCornerShape(AppConstants.SIZE_CORNER_RADIUS_MEDIUM.dp))
            .background(AppColors.surface)
            .border(
                AppConstants.SIZE_BORDER_THICKNESS.dp,
                AppColors.surfaceBorder,
                RoundedCornerShape(AppConstants.SIZE_CORNER_RADIUS_MEDIUM.dp)
            )
            .padding(AppConstants.PADDING_XXLARGE.dp),
        verticalArrangement = Arrangement.spacedBy(AppConstants.SPACING_ZERO.dp),
    ) {
        InfoRow(label = Strings[StringKeys.LABEL_DETECTED_IP]) {
            BasicTextField(
                value = state.localIp,
                onValueChange = onIpChange,
                singleLine = true,
                cursorBrush = SolidColor(AppColors.mono),
                textStyle = TextStyle(
                    fontSize = AppConstants.FONT_SIZE_MEDIUM.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    color = if (state.localIp.isNotBlank()) AppColors.mono else AppColors.error,
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                ),
                modifier = Modifier.width(150.dp),
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
                    fontSize = AppConstants.FONT_SIZE_MEDIUM.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.mono,
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                ),
                modifier = Modifier.width(AppConstants.SIZE_PORT_WIDTH.dp),
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
                fontSize = AppConstants.FONT_SIZE_MEDIUM.sp,
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
            .padding(vertical = AppConstants.PADDING_MEDIUM_V.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontSize = AppConstants.FONT_SIZE_SMALL.sp,
            color = AppColors.textSecondary,
            letterSpacing = AppConstants.LETTER_SPACING_XSMALL.sp
        )
        content()
    }
}

@Composable
private fun HorizontalDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppConstants.SIZE_DIVIDER_HEIGHT.dp)
            .background(AppColors.surfaceBorder)
    )
}

@Composable
private fun ToggleSection(state: ProxyUiState, onToggle: () -> Unit) {
    val canToggle = state.adbStatus == AdbStatus.Available && !state.toggleLoading

    val accentColor by animateColorAsState(
        targetValue = if (state.proxyEnabled) AppColors.success else AppColors.error,
        animationSpec = tween(AppConstants.TOGGLE_ANIM_DURATION_MS),
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (state.proxyEnabled) AppConstants.SIZE_TOGGLE_THUMB_OFFSET_END.dp else AppConstants.SIZE_TOGGLE_THUMB_OFFSET_START.dp,
        animationSpec = tween(AppConstants.TOGGLE_THUMB_ANIM_DURATION_MS),
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppConstants.SPACING_MEDIUM.dp),
    ) {
        Box(
            modifier = Modifier
                .size(
                    width = AppConstants.SIZE_TOGGLE_WIDTH.dp,
                    height = AppConstants.SIZE_TOGGLE_HEIGHT.dp
                )
                .clip(RoundedCornerShape(AppConstants.SIZE_CORNER_RADIUS_LARGE.dp))
                .background(if (state.proxyEnabled) AppColors.successDim else AppColors.surfaceDark)
                .border(
                    AppConstants.SIZE_BORDER_THICK_THICKNESS.dp,
                    accentColor,
                    RoundedCornerShape(AppConstants.SIZE_CORNER_RADIUS_LARGE.dp)
                )
                .clickable(
                    enabled = canToggle,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onToggle,
                ),
            contentAlignment = Alignment.CenterStart,
        ) {
            Box(
                modifier = Modifier
                    .padding(start = thumbOffset)
                    .size(AppConstants.SIZE_TOGGLE_THUMB.dp)
                    .clip(CircleShape)
                    .background(accentColor),
            )
        }

        val labelText = when {
            state.toggleLoading -> Strings[StringKeys.TOGGLE_LOADING]
            state.proxyEnabled -> Strings[StringKeys.TOGGLE_ACTIVE]
            else -> Strings[StringKeys.TOGGLE_INACTIVE]
        }
        Text(
            text = labelText,
            fontSize = AppConstants.FONT_SIZE_SMALL.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = AppConstants.LETTER_SPACING_LARGE.sp,
            color = if (state.toggleLoading) AppColors.textSecondary else accentColor,
        )
    }
}

@Composable
private fun CommandPreview(state: ProxyUiState) {
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
            .clip(RoundedCornerShape(AppConstants.SIZE_CORNER_RADIUS_SMALL.dp))
            .background(AppColors.surfaceDark)
            .border(
                AppConstants.SIZE_BORDER_THICKNESS.dp,
                AppColors.surfaceBorder,
                RoundedCornerShape(AppConstants.SIZE_CORNER_RADIUS_SMALL.dp)
            )
            .padding(AppConstants.PADDING_LARGE.dp),
        verticalArrangement = Arrangement.spacedBy(AppConstants.SPACING_SMALL.dp),
    ) {
        Text(
            text = nextLabel,
            fontSize = AppConstants.FONT_SIZE_XSMALL.sp,
            color = AppColors.textSecondary,
            letterSpacing = AppConstants.LETTER_SPACING_SMALL.sp
        )
        Text(
            text = nextCommand,
            fontSize = AppConstants.FONT_SIZE_SMALL.sp,
            fontFamily = FontFamily.Monospace,
            color = AppColors.mono,
            lineHeight = AppConstants.LINE_HEIGHT_SMALL.sp
        )
    }
}

@Composable
private fun StatusMessage(message: UserMessage?) {
    AnimatedVisibility(
        visible = message != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        message?.let { msg ->
            val (text, color) = when (msg) {
                is UserMessage.Success -> msg.text to AppColors.success
                is UserMessage.Error -> msg.text to AppColors.error
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    fontSize = AppConstants.FONT_SIZE_MEDIUM.sp,
                    color = color,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
