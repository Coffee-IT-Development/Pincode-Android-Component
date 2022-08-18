/*
 * Created by Coffee IT
 *
 * MIT License
 *
 * Copyright (c) 2022 Coffee IT
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package nl.coffeeit.aroma.pincode.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

private const val DEFAULT_SEND_BUTTON_TEXT = "Send code again"
private const val DEFAULT_CORNER_RADIUS = 16

@Composable
fun SendButton(
    sendCooldownDuration: Int = DEFAULT_SEND_COOLDOWN_DURATION,
    onSend: () -> Unit,
    buttonConfiguration: SendButtonConfiguration = DefaultSendButtonConfiguration,
    buttonConfigurationDisabled: SendButtonConfiguration = DefaultSendButtonConfigurationDisabled,
    textStyle: TextStyle = DefaultSendButtonTextStyle,
    disabledTextStyle: TextStyle = DefaultSendButtonDisabledTextStyle,
    sendCodeLiveData: LiveData<Boolean> = MutableLiveData(false)
) {
    var currentTime by remember { mutableStateOf(sendCooldownDuration) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var mutableSendButtonText by remember { mutableStateOf(buttonConfiguration.text) }
    val sendCode: Boolean? by sendCodeLiveData.observeAsState()


    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (currentTime > 0 && isTimerRunning) {
            delay(TimeUnit.SECONDS.toMillis(1))
            currentTime -= 1
        } else {
            isTimerRunning = false
            currentTime = sendCooldownDuration
        }
    }

    LaunchedEffect(key1 = sendCode) {
        if (sendCode == true) {
            onSend()
            isTimerRunning = true
            mutableSendButtonText = buttonConfigurationDisabled.text
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = when (if (isTimerRunning) buttonConfigurationDisabled.alignment else buttonConfiguration.alignment) {
            SendButtonConfiguration.ButtonPosition.START -> {
                Arrangement.Start
            }
            SendButtonConfiguration.ButtonPosition.END -> {
                Arrangement.End
            }
        }
    ) {
        Button(
            onClick = {
                onSend()
                isTimerRunning = true
                mutableSendButtonText = buttonConfigurationDisabled.text
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
                    "$mutableSendButtonText ($currentTime)"
                } else {
                    mutableSendButtonText = buttonConfiguration.text
                    mutableSendButtonText
                },
                fontFamily = if (isTimerRunning) disabledTextStyle.fontFamily else textStyle.fontFamily,
                fontWeight = if (isTimerRunning) disabledTextStyle.fontWeight else textStyle.fontWeight,
                fontStyle = if (isTimerRunning) disabledTextStyle.fontStyle else textStyle.fontStyle
            )
        }
    }
}

class SendButtonConfiguration(
    val text: String = DEFAULT_SEND_BUTTON_TEXT,
    val cornerShape: RoundedCornerShape = RoundedCornerShape(DEFAULT_CORNER_RADIUS.dp),
    val alignment: ButtonPosition = ButtonPosition.START,
) {
    enum class ButtonPosition { START, END }
}