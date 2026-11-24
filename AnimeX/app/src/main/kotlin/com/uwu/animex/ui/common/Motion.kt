package com.uwu.animex.ui.common

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion

object AppMotion {
    const val FADE_OUT_MS = 90
    const val FADE_IN_MS = 210
    const val SHARED_AXIS_MS = 300
    const val FADE_THROUGH_SCALE_MS = 300
    const val IMAGE_CROSSFADE_MS = 180

    const val SHARED_AXIS_SLIDE_DP = 30
    const val FADE_THROUGH_START_SCALE = 0.92f

    fun fadeThrough(): ContentTransform = ContentTransform(EnterTransition.None, ExitTransition.None)

    fun crossfade(motionScheme: MotionScheme): ContentTransform =
        ContentTransform(EnterTransition.None, ExitTransition.None)

    fun sharedAxisXEnter(
        forward: Boolean,
        slidePx: Int,
    ): EnterTransition = EnterTransition.None

    fun sharedAxisXExit(
        forward: Boolean,
        slidePx: Int,
    ): ExitTransition = ExitTransition.None

    @Composable
    fun fadeEnter(): EnterTransition = EnterTransition.None

    @Composable
    fun fadeExit(): ExitTransition = ExitTransition.None

    @Composable
    fun popEnter(): EnterTransition = EnterTransition.None

    @Composable
    fun popExit(): ExitTransition = ExitTransition.None

    @Composable
    fun expandEnter(): EnterTransition = EnterTransition.None

    @Composable
    fun expandExit(): ExitTransition = ExitTransition.None

    @Composable
    fun expandHorizontalEnter(): EnterTransition = EnterTransition.None

    @Composable
    fun expandHorizontalExit(): ExitTransition = ExitTransition.None

    @Composable
    fun slideInEnd(): EnterTransition = EnterTransition.None

    @Composable
    fun slideOutEnd(): ExitTransition = ExitTransition.None
}

@Composable
fun LazyItemScope.animateItemExpressive(): Modifier = Modifier

@Composable
fun <P> rememberHeldPhase(
    target: P,
    loadingPhase: P,
): P = target

private enum class UiPhase { Loading, Error, Ready }

@Composable
fun <T> UiStateContent(
    state: UiState<T>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    loading: @Composable () -> Unit = { GridPlaceholder() },
    error: @Composable (String) -> Unit = { msg -> ErrorState(msg, onRetry) },
    content: @Composable (T) -> Unit,
) {
    val phase =
        when (state) {
            is UiState.Error -> UiPhase.Error
            is UiState.Ready -> UiPhase.Ready
            else -> UiPhase.Loading
        }
    val shownPhase = rememberHeldPhase(phase, UiPhase.Loading)

    var lastReady by remember { mutableStateOf<UiState.Ready<T>?>(null) }
    var lastError by remember { mutableStateOf("") }
    if (state is UiState.Ready<T>) lastReady = state
    if (state is UiState.Error) lastError = state.msg

    Box(modifier.fillMaxSize()) {
        when (shownPhase) {
            UiPhase.Loading -> {
                Box(
                    Modifier.fillMaxSize().clearAndSetSemantics {
                        contentDescription = "Memuat konten"
                        liveRegion = LiveRegionMode.Polite
                    },
                ) { loading() }
            }
            UiPhase.Error -> Box(Modifier.fillMaxSize()) { error(lastError) }
            UiPhase.Ready -> lastReady?.let { content(it.value) }
        }
    }
}
