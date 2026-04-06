package br.com.carvalho.proxyadb.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import br.com.carvalho.proxyadb.core.AppConstants
import br.com.carvalho.proxyadb.core.StringKeys
import br.com.carvalho.proxyadb.core.Strings
import br.com.carvalho.proxyadb.presentation.components.CommandPreview
import br.com.carvalho.proxyadb.presentation.components.InfoCard
import br.com.carvalho.proxyadb.presentation.components.ToggleSection
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
    val os = remember { System.getProperty(AppConstants.SYS_PROP_OS_NAME).lowercase() }
    val topPadding =
        if (os.contains(AppConstants.OS_NAME_MAC)) Dimens.MacTopPadding
        else Dimens.SpacingZero

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
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
        contentPadding = PaddingValues(
            start = Dimens.PaddingScreen,
            end = Dimens.PaddingScreen,
            bottom = Dimens.PaddingScreen,
            top = Dimens.PaddingScreen + topPadding
        ),
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
            fontSize = Dimens.FontSizeLarge,
            fontWeight = FontWeight.Bold,
            color = AppColors.textPrimary,
            letterSpacing = Dimens.LetterSpacingSmall,
        )
        Text(
            text = Strings[StringKeys.APP_SUBTITLE],
            fontSize = Dimens.FontSizeMedium,
            color = AppColors.textSecondary,
            letterSpacing = Dimens.LetterSpacingXSmall,
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
                    fontSize = Dimens.FontSizeMedium,
                    color = color,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
