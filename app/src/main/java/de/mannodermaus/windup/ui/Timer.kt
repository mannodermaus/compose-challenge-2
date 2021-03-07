package de.mannodermaus.windup.ui

import androidx.annotation.IntRange
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.mannodermaus.windup.util.animateLongAsState
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin
import kotlin.time.Duration

@Composable
fun CountdownTimer(
    @IntRange(from = 0)
    value: Long,
    lineThickness: Dp,
    tickColors: List<Color>,
    modifier: Modifier = Modifier,
    isRunning: Boolean = true,
    @IntRange(from = 0)
    max: Long = 60,
    inactiveColor: Color = Color.Gray,
) {
    // Animate changes to the current value
    val animatedValue by animateLongAsState(value)
    val animatedMax by animateLongAsState(max)
    val animatedFontSize by animateDpAsState(if (isRunning) 60.dp else 40.dp)
    val animatedFontColor by animateColorAsState(if (isRunning) {
        MaterialTheme.colors.onSurface
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
    })
    val animatedLineFactor by animateFloatAsState(if (value.toInt() % 2 == 0) {
        0.8f
    } else {
        0.7f
    })

    Box(
        modifier = modifier
            .widthIn(min = 192.dp)
            .aspectRatio(1f)
            .circularTickDial(
                animatedValue,
                animatedMax,
                tickColors,
                inactiveColor,
                animatedLineFactor,
                lineThickness
            )
    ) {
        Text(
            text = formattedTime(animatedValue),
            style = MaterialTheme.typography.h2,
            fontSize = animatedFontSize.value.sp,
            color = animatedFontColor,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private fun formattedTime(value: Long): String {
    val minutes = value / 60
    val seconds = value % 60

    return "${"%02d".format(minutes)}:${"%02d".format(seconds)}"
}

private fun Modifier.circularTickDial(
    value: Long,
    max: Long,
    tickColors: List<Color>,
    inactiveColor: Color,
    lineLengthFactor: Float,
    lineThickness: Dp
) = this.drawBehind {
    rotate(270f, block = {
        val radius = size.minDimension / 2
        val angle = 2 * PI / 60

        val startColor = tickColors.lastOrNull() ?: Color.Black
        val endColor = tickColors.firstOrNull() ?: Color.Black

        for (i in 0..60) {
            // Polar to cartesian
            val direction = Offset(
                x = cos(angle * i).toFloat(),
                y = sin(angle * i).toFloat()
            )

            // Choose color based on the elapsed time
            val fraction =
                ceil(i * (max / 60f)).toInt() // todo this doesn't work fix it pls thx
            val color = if (fraction > value) {
                inactiveColor
            } else {
                lerp(startColor, endColor, i / 60f)
            }

            drawLine(
                color = color,
                start = center + direction * radius * lineLengthFactor,
                end = center + direction * radius,
                strokeWidth = lineThickness.value,
                cap = StrokeCap.Round
            )
        }
    })
}

@Composable
fun TimerKeypad(
    onDigit: (Int) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            TimerInputButton(text = "1", onClick = { onDigit(1) })
            TimerInputButton(text = "2", onClick = { onDigit(2) })
            TimerInputButton(text = "3", onClick = { onDigit(3) })
        }
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            TimerInputButton(text = "4", onClick = { onDigit(4) })
            TimerInputButton(text = "5", onClick = { onDigit(5) })
            TimerInputButton(text = "6", onClick = { onDigit(6) })
        }
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            TimerInputButton(text = "7", onClick = { onDigit(7) })
            TimerInputButton(text = "8", onClick = { onDigit(8) })
            TimerInputButton(text = "9", onClick = { onDigit(9) })
        }
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            TimerInputButton(text = "", onClick = {})
            TimerInputButton(text = "0", onClick = { onDigit(0) })
            TimerInputButton(text = "\u232b", onClick = { onBackspace() })
        }
    }
}

@Composable
private fun TimerInputButton(text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        enabled = text.isNotBlank(),
        modifier = Modifier
            .height(80.dp)
            .aspectRatio(1f),
    ) {
        Text(
            style = MaterialTheme.typography.h3,
            text = text
        )
    }
}
