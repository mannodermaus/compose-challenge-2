/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
