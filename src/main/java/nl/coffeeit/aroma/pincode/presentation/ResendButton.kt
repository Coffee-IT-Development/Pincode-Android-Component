package nl.coffeeit.aroma.pincode.presentation

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import kotlinx.coroutines.delay

@Composable
fun ResendButton(
    totalTime: Int,
    onResendButton: () -> Unit,
    resendButtonText: String
) {
    var currentTime by remember { mutableStateOf(totalTime) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var mutableResendButtonText by remember { mutableStateOf(resendButtonText) }

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (currentTime > 0 && isTimerRunning) {
            delay(1000)
            currentTime -= 1
        } else {
            isTimerRunning = false
            currentTime = totalTime
        }
    }

    Button(
        onClick = {
            onResendButton()
            isTimerRunning = true
        },
        enabled = !isTimerRunning,
        modifier = if (isTimerRunning) Modifier.alpha(0.5f) else Modifier
    ) {
        Text(text = "$mutableResendButtonText ($currentTime)")
    }
}

enum class ButtonPosition { START, END }