package de.mannodermaus.windup.util

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

/**
 * I guess there is no animateState() for Long values yet, oh well copy-paste here we go
 */
@Composable
fun animateLongAsState(
    targetValue: Long,
    animationSpec: AnimationSpec<Long> = longDefaultSpring,
    finishedListener: ((Long) -> Unit)? = null
): State<Long> {
    return animateValueAsState(
        targetValue, Long.VectorConverter, animationSpec, finishedListener = finishedListener
    )
}

private val longDefaultSpring = spring(visibilityThreshold = Long.VisibilityThreshold)

val Long.Companion.VectorConverter: TwoWayConverter<Long, AnimationVector1D>
    get() = LongToVector

private val LongToVector: TwoWayConverter<Long, AnimationVector1D> =
    TwoWayConverter({ AnimationVector1D(it.toFloat()) }, { it.value.toLong() })

val Long.Companion.VisibilityThreshold: Long
    get() = 1L
