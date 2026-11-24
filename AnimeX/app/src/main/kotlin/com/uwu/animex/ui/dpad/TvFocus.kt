package com.uwu.animex.ui.dpad

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CornerRadius
import androidx.compose.ui.graphics.Stroke
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class TvFocusIndication(
    val color: Color,
    val density: Density,
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode =
        TvFocusNode(interactionSource, color, density)
}

private class TvFocusNode(
    private val interactionSource: InteractionSource,
    private val color: Color,
    private val density: Density,
) : Modifier.Node(), DrawModifierNode {
    private var focused = false

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect { interaction ->
                val next =
                    when (interaction) {
                        is FocusInteraction.Focus -> true
                        is FocusInteraction.Unfocus -> false
                        else -> focused
                    }
                if (next != focused) {
                    focused = next
                    invalidateDraw()
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        drawContent()
        if (!focused) return
        val stroke = with(density) { 3.dp.toPx() }
        val radius = CornerRadius(with(density) { 10.dp.toPx() })
        drawRoundRect(
            color = color,
            topLeft = Offset(-stroke, -stroke),
            size = Size(size.width + stroke * 2, size.height + stroke * 2),
            cornerRadius = radius,
            style = Stroke(width = stroke),
        )
    }
}

@Composable
fun TvFocusProvider(content: @Composable () -> Unit) {
    val color = MaterialTheme.colorScheme.primary
    val density = LocalDensity.current
    CompositionLocalProvider(LocalIndication provides TvFocusIndication(color, density)) {
        content()
    }
}
