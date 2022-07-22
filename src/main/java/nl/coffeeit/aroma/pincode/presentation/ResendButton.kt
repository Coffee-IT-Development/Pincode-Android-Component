package nl.coffeeit.aroma.pincode.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private const val DEFAULT_RESEND_BUTTON_TEXT = "Send code again"
private const val DEFAULT_CORNER_RADIUS = 16
private const val DEFAULT_TIME_BEFORE_RESEND_IN_SECONDS = 60

private val DefaultResendButtonPosition = ResendButtonStyle.ButtonPosition.START

@Composable
fun ResendButton(
    totalTime: Int = DEFAULT_TIME_BEFORE_RESEND_IN_SECONDS,
    onResendButton: () -> Unit,
    buttonStyle: ResendButtonStyle
) {
    var currentTime by remember { mutableStateOf(totalTime) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var mutableResendButtonText by remember { mutableStateOf(buttonStyle.text) }

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (currentTime > 0 && isTimerRunning) {
            delay(1000)
            currentTime -= 1
        } else {
            isTimerRunning = false
            currentTime = totalTime
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = when (buttonStyle.alignment) {
            ResendButtonStyle.ButtonPosition.START -> {
                Arrangement.Start
            }
            ResendButtonStyle.ButtonPosition.END -> {
                Arrangement.End
            }
        }
    ) {
        Button(
            onClick = {
                onResendButton()
                isTimerRunning = true
            },
            shape = buttonStyle.cornerShape,
            enabled = !isTimerRunning,
            colors = ButtonDefaults.buttonColors(backgroundColor = buttonStyle.backgroundColor),
            modifier = (if (isTimerRunning) Modifier.alpha(0.5f) else Modifier)
        ) {
            Text(
                text = if (isTimerRunning) {
                    "$mutableResendButtonText ($currentTime)"
                } else { mutableResendButtonText },
                color = buttonStyle.textColor
            )
        }
    }
}

class ResendButtonStyle(
    val text: String = DEFAULT_RESEND_BUTTON_TEXT,
    val textColor: Color = Color.White,
    val backgroundColor: Color = Color(0xFF6650a4),
    val cornerShape: RoundedCornerShape = RoundedCornerShape(DEFAULT_CORNER_RADIUS.dp),
    val alignment: ButtonPosition = DefaultResendButtonPosition
) {
    enum class ButtonPosition { START, END }
}

