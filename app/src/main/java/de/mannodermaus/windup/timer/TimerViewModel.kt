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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.time.Duration
import kotlin.time.seconds

class TimerViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private var _timeEvents = MutableStateFlow(Time(60, 60))
    val timeEvents: StateFlow<Time> get() = _timeEvents

    private var _stateEvents = MutableStateFlow(TimerState.Stopped)
    val stateEvents: StateFlow<TimerState> get() = _stateEvents

    val completionEvents = Channel<Unit>()

    private var job: Job? = null

    fun addTime(amount: Duration) {
        // The "total" time of the timer only increases
        // if the added amount pushes beyond its current threshold
        val current = _timeEvents.value
        val newRemaining = (current.remainingSeconds + amount.inSeconds).toLong()
        val newTotal = max(current.totalSeconds, newRemaining)

        _timeEvents.value = Time(
            totalSeconds = newTotal,
            remainingSeconds = newRemaining,
        )
    }

    fun subtractTime(amount: Duration) {
        // 0 is the lowest the timer can go
        val current = _timeEvents.value

        _timeEvents.value = current.copy(
            remainingSeconds = max(0.0, current.remainingSeconds - amount.inSeconds).toLong()
        )
    }

    fun startTimer() {
        stopTimer()

        if (currentRemaining <= 0L) {
            return
        }

        job = viewModelScope.launch(dispatcher) {
            // Tick away, little bird
            while (currentRemaining > 0) {
                delay(1.seconds)
                subtractTime(1.seconds)
            }

            // Notify completion and clean up
            completionEvents.send(Unit)
            stopTimer()
        }
        _stateEvents.value = TimerState.Running
    }

    fun stopTimer() {
        job?.cancel()
        job = null
        _stateEvents.value = TimerState.Stopped
    }

    private val currentRemaining get() = _timeEvents.value.remainingSeconds

    /* Types */

    data class Time(
        val totalSeconds: Long,
        val remainingSeconds: Long
    )

    enum class TimerState {
        Stopped,
        Running
    }
}
