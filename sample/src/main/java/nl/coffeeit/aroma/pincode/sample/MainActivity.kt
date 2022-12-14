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

package nl.coffeeit.aroma.pincode.sample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import nl.coffeeit.aroma.pincode.presentation.PincodeView
import nl.coffeeit.aroma.pincode.presentation.SendButtonConfiguration
import nl.coffeeit.aroma.pincode.sample.databinding.ActivityMainBinding

private const val FULL_ALPHA = 1
private const val MILLISECOND = 1L
private const val PROGRESS_PER_MILLISECOND = 0.01f

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val isError = MutableLiveData(false)
    private val handler = Handler(Looper.getMainLooper())
    private val pincode = MutableLiveData("")
    private val sendCode = MutableLiveData<Boolean>()

    private val fontFamily = FontFamily(
        Font(R.font.inter_thin, FontWeight.Thin),
        Font(R.font.inter_extralight, FontWeight.ExtraLight),
        Font(R.font.inter_light, FontWeight.Light),
        Font(R.font.inter_regular, FontWeight.Normal),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_semibold, FontWeight.SemiBold),
        Font(R.font.inter_bold, FontWeight.Bold),
        Font(R.font.inter_extrabold, FontWeight.ExtraBold),
        Font(R.font.inter_black, FontWeight.Black)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.cvPincode.setContent {
            val progress = MutableLiveData(0f)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp)
            ) {
                Image(
                    modifier = Modifier
                        .width(231.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(R.drawable.ill_aroma_logo),
                    contentDescription = "Coffee IT's Aroma logo",
                    contentScale = ContentScale.FillWidth
                )

                Spacer(modifier = Modifier.height(75.dp))

                PincodeView(
                    pincodeLiveData = pincode,
                    isErrorLiveData = isError,
                    showDividerAfterInput = 3,
                    enableSendButton = true,
                    sendButtonConfiguration = SendButtonConfiguration(),
                    focusedBorderThickness = 2.dp,
                    unfocusedBorderThickness = 2.dp,
                    inputSpacing = 8.dp,
                    dividerHeight = 3.dp,
                    dividerWidth = 10.dp,
                    dividerColor = Color(0xFF3F3D56),
                    inputColors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF4D6CDB),
                        unfocusedBorderColor = Color(0xFFDDDFE4),
                        backgroundColor = Color(0xFFDDDFE4),
                        errorBorderColor = Color(0xFFFF7A7A),
                        textColor = Color.White,
                        cursorColor = Color.Transparent,
                        errorCursorColor = Color.Transparent
                    ),
                    inputTextStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF3F3D56),
                        fontSize = 26.sp
                    ),
                    inputErrorTextStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFF7A7A),
                        fontSize = 26.sp
                    ),
                    sendButtonTextStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF3F3D56),
                        fontSize = 15.sp,
                        background = Color(0xFFDDDFE4)
                    ),
                    sendButtonDisabledTextStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF3F3D56),
                        fontSize = 15.sp,
                        background = Color(0x8B4D6CDB)
                    ),
                    errorLabelTextStyle = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFF7A7A),
                        fontSize = 14.sp
                    ),
                    onPincodeCompleted = {
                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed(object : Runnable {
                            override fun run() {
                                val newProgress = (progress.value ?: 0f) + PROGRESS_PER_MILLISECOND
                                if (newProgress > FULL_ALPHA) {
                                    handler.removeCallbacksAndMessages(null)
                                    isError.postValue(true)
                                    progress.postValue(0f)
                                    return
                                } else {
                                    progress.postValue(newProgress)
                                }
                                handler.postDelayed(this, MILLISECOND)
                            }
                        }, MILLISECOND)
                    },
                    keyEventInErrorState = {
                        isError.postValue(false)
                    },
                    autoFocusFirstInput = true,
                    errorText = "Wrong code entered",
                    onBack = { finish() },
                    sendCodeLiveData = sendCode
                )

                Spacer(modifier = Modifier.height(8.dp))

                RoundedCircularProgressIndicator(
                    progressLiveData = progress,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color(0xFF4D6CDB),
                    backgroundColor = Color(0xFFE1E2E2),
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        sendCode.postValue(true)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}