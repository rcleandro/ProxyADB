package br.com.carvalho.proxyadb.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import br.com.carvalho.proxyadb.core.StringKeys
import br.com.carvalho.proxyadb.core.Strings
import br.com.carvalho.proxyadb.presentation.AdbStatus
import br.com.carvalho.proxyadb.presentation.AppColors
import br.com.carvalho.proxyadb.presentation.Dimens
import br.com.carvalho.proxyadb.presentation.ProxyUiState

@Composable
fun ToggleSection(state: ProxyUiState, onToggle: () -> Unit) {
    val canToggle = state.adbStatus == AdbStatus.Available && !state.toggleLoading

    val accentColor by animateColorAsState(
        targetValue = if (state.proxyEnabled) AppColors.success else AppColors.error,
        animationSpec = tween(Dimens.ANIM_TOGGLE_MS),
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (state.proxyEnabled) Dimens.SizeToggleThumbOffsetEnd else Dimens.SizeToggleThumbOffsetStart,
        animationSpec = tween(Dimens.ANIM_THUMB_MS),
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
    ) {
        Box(
            modifier = Modifier
                .size(
                    width = Dimens.SizeToggleWidth,
                    height = Dimens.SizeToggleHeight
                )
                .clip(RoundedCornerShape(Dimens.CornerRadiusLarge))
                .background(if (state.proxyEnabled) AppColors.successDim else AppColors.surfaceDark)
                .border(
                    Dimens.SizeBorderThickThickness,
                    accentColor,
                    RoundedCornerShape(Dimens.CornerRadiusLarge)
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
                    .size(Dimens.SizeToggleThumb)
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
            fontSize = Dimens.FontSizeSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = Dimens.LetterSpacingLarge,
            color = if (state.toggleLoading) AppColors.textSecondary else accentColor,
        )
    }
}
