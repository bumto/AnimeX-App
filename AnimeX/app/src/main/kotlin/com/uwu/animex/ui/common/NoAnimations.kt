package com.uwu.animex.ui.common

import androidx.compose.animation.AnimatedContent as FrameworkAnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility as FrameworkAnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.snap
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

object NoMotionScheme : MotionScheme {
    override fun <T : Any?> defaultEffectsSpec(): FiniteAnimationSpec<T> = snap()

    override fun <T : Any?> fastEffectsSpec(): FiniteAnimationSpec<T> = snap()

    override fun <T : Any?> slowEffectsSpec(): FiniteAnimationSpec<T> = snap()

    override fun <T : Any?> defaultSpatialSpec(): FiniteAnimationSpec<T> = snap()

    override fun <T : Any?> fastSpatialSpec(): FiniteAnimationSpec<T> = snap()

    override fun <T : Any?> slowSpatialSpec(): FiniteAnimationSpec<T> = snap()
}

fun Modifier.animateContentSize(
    animationSpec: AnimationSpec<IntSize> = snap(),
    finishedListener: ((IntSize) -> Unit)? = null,
): Modifier = this

class Animatable<T>(
    initialValue: T,
    vararg ignored: Any?,
) {
    var value: T by mutableStateOf(initialValue)
        private set

    var targetValue: T by mutableStateOf(initialValue)
        private set

    suspend fun animateTo(
        targetValue: T,
        animationSpec: AnimationSpec<T> = snap(),
        finishedListener: ((T) -> Unit)? = null,
    ) {
        value = targetValue
        this.targetValue = targetValue
        finishedListener?.invoke(targetValue)
    }

    fun snapTo(targetValue: T) {
        value = targetValue
        this.targetValue = targetValue
    }

    suspend fun stop() = Unit

    fun asState(): State<T> = derivedStateOf { value }
}

@Composable
fun <T> animateValueAsState(
    targetValue: T,
    animationSpec: AnimationSpec<T> = snap(),
    label: String = "",
    finishedListener: ((T) -> Unit)? = null,
): State<T> {
    finishedListener?.invoke(targetValue)
    return rememberUpdatedState(targetValue)
}

@Composable
fun animateFloatAsState(
    targetValue: Float,
    animationSpec: AnimationSpec<Float> = snap(),
    label: String = "",
    finishedListener: ((Float) -> Unit)? = null,
): State<Float> = animateValueAsState(targetValue, animationSpec, label, finishedListener)

@Composable
fun animateDpAsState(
    targetValue: Dp,
    animationSpec: AnimationSpec<Dp> = snap(),
    label: String = "",
    finishedListener: ((Dp) -> Unit)? = null,
): State<Dp> = animateValueAsState(targetValue, animationSpec, label, finishedListener)

@Composable
fun animateColorAsState(
    targetValue: Color,
    animationSpec: AnimationSpec<Color> = snap(),
    label: String = "",
    finishedListener: ((Color) -> Unit)? = null,
): State<Color> = animateValueAsState(targetValue, animationSpec, label, finishedListener)

@Composable
fun animateIntAsState(
    targetValue: Int,
    animationSpec: AnimationSpec<Int> = snap(),
    label: String = "",
    finishedListener: ((Int) -> Unit)? = null,
): State<Int> = animateValueAsState(targetValue, animationSpec, label, finishedListener)

@Composable
fun animateIntOffsetAsState(
    targetValue: IntOffset,
    animationSpec: AnimationSpec<IntOffset> = snap(),
    label: String = "",
    finishedListener: ((IntOffset) -> Unit)? = null,
): State<IntOffset> = animateValueAsState(targetValue, animationSpec, label, finishedListener)

@Composable
fun animateIntSizeAsState(
    targetValue: IntSize,
    animationSpec: AnimationSpec<IntSize> = snap(),
    label: String = "",
    finishedListener: ((IntSize) -> Unit)? = null,
): State<IntSize> = animateValueAsState(targetValue, animationSpec, label, finishedListener)

@Composable
fun animateOffsetAsState(
    targetValue: Offset,
    animationSpec: AnimationSpec<Offset> = snap(),
    label: String = "",
    finishedListener: ((Offset) -> Unit)? = null,
): State<Offset> = animateValueAsState(targetValue, animationSpec, label, finishedListener)

@Composable
fun animateSizeAsState(
    targetValue: Size,
    animationSpec: AnimationSpec<Size> = snap(),
    label: String = "",
    finishedListener: ((Size) -> Unit)? = null,
): State<Size> = animateValueAsState(targetValue, animationSpec, label, finishedListener)

@Composable
fun AnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = EnterTransition.None,
    exit: ExitTransition = ExitTransition.None,
    label: String = "",
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    FrameworkAnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = EnterTransition.None,
        exit = ExitTransition.None,
        content = content,
    )
}

@Composable
fun <T> AnimatedContent(
    targetState: T,
    modifier: Modifier = Modifier,
    transitionSpec: AnimatedContentTransitionScope<T>.() -> ContentTransform = {
        ContentTransform(EnterTransition.None, ExitTransition.None)
    },
    contentKey: (targetState: T) -> Any? = { it },
    label: String = "",
    content: @Composable AnimatedContentScope.(targetState: T) -> Unit,
) {
    FrameworkAnimatedContent(
        targetState = targetState,
        modifier = modifier,
        transitionSpec = { ContentTransform(EnterTransition.None, ExitTransition.None) },
        contentKey = contentKey,
        content = content,
    )
}

class NoInfiniteTransition {
    @Composable
    fun animateFloat(
        initialValue: Float,
        targetValue: Float,
        animationSpec: AnimationSpec<Float> = snap(),
        label: String = "",
    ): State<Float> = rememberUpdatedState(initialValue)

    @Composable
    fun animateColor(
        initialValue: Color,
        targetValue: Color,
        animationSpec: AnimationSpec<Color> = snap(),
        label: String = "",
    ): State<Color> = rememberUpdatedState(initialValue)

    @Composable
    fun <T> animateValue(
        initialValue: T,
        targetValue: T,
        typeConverter: Any? = null,
        animationSpec: AnimationSpec<T> = snap(),
        label: String = "",
    ): State<T> = rememberUpdatedState(initialValue)
}

@Composable
fun rememberInfiniteTransition(label: String = ""): NoInfiniteTransition =
    remember { NoInfiniteTransition() }
