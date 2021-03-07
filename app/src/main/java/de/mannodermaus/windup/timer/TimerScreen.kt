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
package de.mannodermaus.windup.timer

import android.os.Vibrator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import de.mannodermaus.windup.ui.CountdownTimer
import de.mannodermaus.windup.ui.theme.beige
import de.mannodermaus.windup.ui.theme.orange
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.time.Duration
import kotlin.time.seconds

@Composable
fun MainScreen(
    viewModel: TimerViewModel = viewModel()
) {
    // State
    val time by viewModel.timeEvents.collectAsState()
    val state by viewModel.stateEvents.collectAsState()
    val isRunning = state == TimerViewModel.TimerState.Running

    // Effects
    val context = LocalContext.current
    LaunchedEffect(viewModel) {
        // Vibrate when the timer reaches zero
        val vibrator = context.getSystemService<Vibrator>()!!
        viewModel.completionEvents.receiveAsFlow().collect {
            vibrator.vibrate(500L) // TODO Use that other, cooler API on eligible devices
        }
    }

    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CountdownTimer(
                modifier = Modifier.fillMaxWidth(0.75f),
                value = time.remainingSeconds,
                max = time.totalSeconds,
                isRunning = isRunning,
                lineThickness = 8.dp,
                tickColors = listOf(orange, beige),
                inactiveColor = Color(0xFFDDDDDD),
            )

            Spacer(modifier = Modifier.height(24.dp))

            TimerButtons(
                modifier = Modifier.fillMaxWidth(0.5f),
                isRunning = isRunning,
                canBeToggled = time.remainingSeconds > 0L,
                onAddTime = viewModel::addTime,
                onSubtractTime = viewModel::subtractTime,
                onToggle = {
                    if (isRunning) {
                        viewModel.stopTimer()
                    } else {
                        viewModel.startTimer()
                    }
                }
            )
        }
    }
}

@Composable
private fun TimerButtons(
    isRunning: Boolean,
    canBeToggled: Boolean,
    onAddTime: (Duration) -> Unit,
    onSubtractTime: (Duration) -> Unit,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { onAddTime(15.seconds) }) {
                Text("+15 sec")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { onSubtractTime(15.seconds) }) {
                Text("-15 sec")
            }
        }
        Spacer(Modifier.height(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = canBeToggled,
            onClick = { onToggle() }
        ) {
            Text(
                if (isRunning) {
                    "Pause"
                } else {
                    "Start"
                }
            )
        }
    }
}
