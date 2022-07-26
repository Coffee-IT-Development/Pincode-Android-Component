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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

private const val DEFAULT_RESEND_BUTTON_TEXT = "Send code again"
private const val DEFAULT_CORNER_RADIUS = 16

@Composable
fun ResendButton(
    resendCooldownDuration: Int = DEFAULT_RESEND_COOLDOWN_DURATION,
    onResend: () -> Unit,
    buttonConfiguration: ResendButtonConfiguration = DefaultResendButtonConfiguration,
    buttonConfigurationDisabled: ResendButtonConfiguration = DefaultResendButtonConfigurationDisabled,
    textStyle: TextStyle = DefaultResendButtonTextStyle,
    disabledTextStyle: TextStyle = DefaultResendButtonDisabledTextStyle,
) {
    var currentTime by remember { mutableStateOf(resendCooldownDuration) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var mutableResendButtonText by remember { mutableStateOf(buttonConfiguration.text) }

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (currentTime > 0 && isTimerRunning) {
            delay(TimeUnit.SECONDS.toMillis(1))
            currentTime -= 1
        } else {
            isTimerRunning = false
            currentTime = resendCooldownDuration
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = when (if (isTimerRunning) buttonConfigurationDisabled.alignment else buttonConfiguration.alignment) {
            ResendButtonConfiguration.ButtonPosition.START -> {
                Arrangement.Start
            }
            ResendButtonConfiguration.ButtonPosition.END -> {
                Arrangement.End
            }
        }
    ) {
        Button(
            onClick = {
                onResend()
                isTimerRunning = true
                mutableResendButtonText = buttonConfigurationDisabled.text
            },
            shape = if (isTimerRunning) buttonConfigurationDisabled.cornerShape else buttonConfiguration.cornerShape,
            enabled = !isTimerRunning,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = textStyle.background,
                disabledBackgroundColor = disabledTextStyle.background,
                contentColor = textStyle.color,
                disabledContentColor = disabledTextStyle.color
            ),
        ) {
            Text(
                text = if (isTimerRunning) {
                    "$mutableResendButtonText ($currentTime)"
                } else {
                    mutableResendButtonText = buttonConfiguration.text
                    mutableResendButtonText
                },
                fontFamily = if (isTimerRunning) disabledTextStyle.fontFamily else textStyle.fontFamily,
                fontWeight = if (isTimerRunning) disabledTextStyle.fontWeight else textStyle.fontWeight,
                fontStyle = if (isTimerRunning) disabledTextStyle.fontStyle else textStyle.fontStyle
            )
        }
    }
}

class ResendButtonConfiguration(
    val text: String = DEFAULT_RESEND_BUTTON_TEXT,
    val cornerShape: RoundedCornerShape = RoundedCornerShape(DEFAULT_CORNER_RADIUS.dp),
    val alignment: ButtonPosition = ButtonPosition.START,
) {
    enum class ButtonPosition { START, END }
}