package nl.coffeeit.aroma.pincode.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import nl.coffeeit.aroma.pincode.domain.model.PincodeItem
import nl.coffeeit.aroma.pincode.extension.digits
import nl.coffeeit.aroma.pincode.extension.digitsAndLetters

private const val DEFAULT_CORNER_RADIUS = 8
private const val DEFAULT_LENGTH_OF_CODE = 6
private const val KEYBOARD_OPEN_DELAY_IN_MILLIS = 100L
private const val MAXIMUM_AMOUNT_OF_CHARACTERS_PER_INPUT = 1

private val DefaultBackgroundColor = Color(0xFFF6F6F6)
private val DefaultDividerColor = Color(0xFF625b71)
private val DefaultErrorColor = Color(0xFFF7694A)
private val DefaultFocusedBorderColor = Color(0xFF6650a4)
private val DefaultUnfocusedBorderColor = Color(0xFF625b71)

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun PincodeView(
    lengthOfCode: Int = DEFAULT_LENGTH_OF_CODE,
    inputWidth: Dp? = null,
    inputCornerShape: RoundedCornerShape = RoundedCornerShape(DEFAULT_CORNER_RADIUS.dp),
    inputColors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = DefaultFocusedBorderColor,
        unfocusedBorderColor = DefaultUnfocusedBorderColor,
        errorBorderColor = DefaultErrorColor,
        errorLabelColor = DefaultErrorColor,
        backgroundColor = DefaultBackgroundColor,
        cursorColor = Color.Transparent,
        errorCursorColor = Color.Transparent
    ),
    inputSpacing: Dp = 16f.dp,
    showDividerAfterInput: Int? = null,
    dividerColor: Color = DefaultDividerColor,
    dividerHeight: Dp = 2.dp,
    dividerWidth: Dp = 6.dp,
    modifier: Modifier = Modifier.padding(horizontal = 16.dp),
    errorText: String? = null,
    errorLabelPaddingVertical: Dp = 8.dp,
    focusedBorderThickness: Dp = 1.dp,
    unfocusedBorderThickness: Dp = 1.dp,
    inputTextStyle: TextStyle = TextStyle(
        textAlign = TextAlign.Center,
        color = Color.Black
    ),
    inputErrorTextStyle: TextStyle = TextStyle(
        textAlign = TextAlign.Center,
        color = Color.Black
    ),
    errorLabelTextStyle: TextStyle = TextStyle(
        color = DefaultErrorColor
    ),
    onlyDigits: Boolean = true,
    autoFocusFirstInput: Boolean = false,
    pincodeLiveData: LiveData<String>,
    isErrorLiveData: LiveData<Boolean>,
    resetPincodeLiveData: () -> Unit = { },
    onBack: () -> Unit = { },
    onPincodeCompleted: (String?) -> Unit = { }
) {
    val isError: Boolean? by isErrorLiveData.observeAsState()
    val pincode: String? by pincodeLiveData.observeAsState()

    val enteredValues by remember {
        mutableStateOf(mutableListOf<PincodeItem>().apply {
            for (i in 0 until lengthOfCode) {
                add(PincodeItem(i, ""))
            }
        })
    }
    var mutablePincode by remember { mutableStateOf(pincode) }

    Column(
        modifier = modifier
    ) {
        Row(
            Modifier
                .fillMaxWidth()
        ) {
            val dividerModifier = Modifier
                .height(dividerHeight)
                .width(dividerWidth)
                .align(alignment = Alignment.CenterVertically)
                .graphicsLayer {
                    shape = inputCornerShape
                    clip = true
                }

            if (showDividerAfterInput == 0) {
                DividerWithSpacerEnd(inputSpacing, dividerColor, dividerModifier)
            }

            for (i in 0 until lengthOfCode) {
                val clipboardManager = LocalClipboardManager.current
                val focusManager = LocalFocusManager.current
                val focusRequester = remember { FocusRequester() }
                val interactionSource = remember { MutableInteractionSource() }
                val isFirstInput = i == 0
                val isLastInput = i == lengthOfCode - 1
                val keyboard = LocalSoftwareKeyboardController.current
                // Use TextFieldValue instead of String to prevent the character replacement bug from happening.
                // This bug happens when a character is added to an input when the cursor is in a position
                // that is not 0. In this case, onValueChanged will receive the character that was already
                // in the input twice. For example, if there is "W" in the input and "2" is added,
                // onValueChanged will receive "WW", while "W2" or "2W" is expected. TextFieldValue
                // sets the selection (position of the cursor) to 0 by default. In the same example
                // this means that when there is "W" in the input and "2" is added, onValueChanged will
                // now receive "2W".
                var pincodeCharacterTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

                // Fill out pincode from parameters or from pasted value
                if (mutablePincode?.isNotEmpty() == true) {

                    // Add next character and remove it from the total string
                    val character = mutablePincode?.substring(0, 1)
                    mutablePincode = mutablePincode?.drop(1)
                    (if (onlyDigits) character?.digits() else character?.digitsAndLetters())?.let { validatedText ->
                        pincodeCharacterTextFieldValue = TextFieldValue(validatedText)
                        enteredValues.find { pincodeItem -> pincodeItem.index == i }?.text = validatedText

                        // Send code back to parent composable if this is the last input and it's not empty
                        if (validatedText.isNotEmpty() && isLastInput) {
                            val completedPincode =
                                enteredValues.joinToString("") { pincodeItem -> pincodeItem.text }
                            onPincodeCompleted(completedPincode)
                        }
                    }

                    // Move focus to next input if current input is not the last input and the total string
                    // is not empty yet
                    if (mutablePincode?.isNotEmpty() == true && !isLastInput) {
                        focusManager.moveFocus(FocusDirection.Right)
                    }
                }

                val cellModifier = if (inputWidth != null) {
                    Modifier
                        .width(inputWidth)
                        .wrapContentHeight()
                } else {
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .wrapContentHeight()

                }
                    .background(
                        color = inputColors.backgroundColor(enabled = true).value,
                        shape = inputCornerShape
                    )
                    .onKeyEvent {
                        // Clear pincode from parameters or from pasted value when a key is pressed
                        mutablePincode = ""

                        // If pressed key is backspace, remove value from input and clear the value in the list
                        if (it.key == Key.Backspace) {
                            pincodeCharacterTextFieldValue = TextFieldValue("")
                            enteredValues.find { pincodeItem -> pincodeItem.index == i }?.text = ""

                            // Move focus to previous input if current input is not the first input
                            if (!isFirstInput) {
                                focusManager.moveFocus(FocusDirection.Left)
                            }
                        }

                        if (it.key == Key.Back) {
                            onBack()
                        }
                        return@onKeyEvent true
                    }
                    .focusRequester(focusRequester)

                BasicTextField(
                    value = pincodeCharacterTextFieldValue,
                    onValueChange = { textFieldValue ->
                        var text = textFieldValue.text

                        // The previous value should be removed, so the new value can overwrite it
                        enteredValues.find { it.index == i }?.text?.let { pincodeItemText ->
                            if (pincodeItemText.isNotEmpty()) {
                                text = text.removeRange(
                                    text.lastIndexOf(pincodeItemText),
                                    text.lastIndexOf(pincodeItemText) + 1
                                )
                            }
                        }

                        // Only handle the change when the text changed
                        if (enteredValues.find { enteredValue -> enteredValue.index == i }?.text != textFieldValue.text) {
                            
                            // Check if the value is the same as the copied text
                            if (text == clipboardManager.getText()?.text) {
                                mutablePincode =
                                    if (onlyDigits) text.digits() else text.digitsAndLetters()

                                // If the pasted value is longer than the length of the code, cut all
                                // characters that exceed the length off
                                if ((mutablePincode?.length ?: 0) > lengthOfCode) {
                                    mutablePincode = mutablePincode?.substring(0, lengthOfCode)
                                }

                                // Request focus on the first input
                                for (j in 1..i) {
                                    focusManager.moveFocus(FocusDirection.Left)
                                }
                            } else {
                                // Add single character to input

                                var validatedText =
                                    if (onlyDigits) text.digits() else text.digitsAndLetters()

                                // It is possible that two or more characters or more are potentially added, for example
                                // when spamming the keyboard. To prevent multiple characters from being added, only the first
                                // character is used
                                if (validatedText.length > MAXIMUM_AMOUNT_OF_CHARACTERS_PER_INPUT) {
                                    validatedText = validatedText.substring(0, 1)
                                }

                                // Place the value in the input if it's shorter than or has the same size as the maximum
                                // amount of characters per input
                                if (validatedText.length <= MAXIMUM_AMOUNT_OF_CHARACTERS_PER_INPUT) {
                                    pincodeCharacterTextFieldValue = TextFieldValue(validatedText)

                                    // Move focus to next input if the current input is not the
                                    // last input and value is not empty
                                    if (validatedText.isNotEmpty() && !isLastInput) {
                                        focusManager.moveFocus(FocusDirection.Right)
                                    }

                                    // Save the entered value in the list
                                    enteredValues.find { pincodeItem -> pincodeItem.index == i }?.text =
                                        validatedText

                                    // Send code back to parent composable if this is the last input and it's not empty
                                    if (validatedText.isNotEmpty() && isLastInput) {
                                        val completedPincode =
                                            enteredValues.joinToString("") { pincodeItem -> pincodeItem.text }
                                        onPincodeCompleted(completedPincode)
                                    }
                                }
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = if (isLastInput) ImeAction.Done else ImeAction.Next,
                        autoCorrect = false,
                        keyboardType = if (onlyDigits) KeyboardType.Number else KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Right) },
                        onDone = { focusManager.clearFocus() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    textStyle = if (isError == true) inputErrorTextStyle else inputTextStyle,
                    interactionSource = interactionSource,
                    cursorBrush = SolidColor(inputColors.cursorColor(isError = isError == true).value),
                    modifier = cellModifier
                ) { innerTextField ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextFieldDefaults.OutlinedTextFieldDecorationBox(
                            value = pincodeCharacterTextFieldValue.text,
                            visualTransformation = VisualTransformation.None,
                            innerTextField = innerTextField,
                            singleLine = true,
                            enabled = true,
                            contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                                start = 0.dp,
                                end = 0.dp
                            ),
                            isError = isError == true,
                            interactionSource = interactionSource,
                            border = {
                                TextFieldDefaults.BorderBox(
                                    enabled = true,
                                    isError = isError == true,
                                    interactionSource = interactionSource,
                                    colors = inputColors,
                                    shape = inputCornerShape,
                                    unfocusedBorderThickness = unfocusedBorderThickness,
                                    focusedBorderThickness = focusedBorderThickness
                                )
                            }
                        )
                    }
                }

                if (autoFocusFirstInput && isFirstInput) {
                LaunchedEffect(focusRequester) {
                        focusRequester.requestFocus()
                        // Add a delay, otherwise the keyboard doesn't open
                        delay(KEYBOARD_OPEN_DELAY_IN_MILLIS)
                        keyboard?.show()
                    }
                }

                if (i + 1 == showDividerAfterInput) {
                    DividerWithSpacerStart(inputSpacing, dividerColor, dividerModifier)
                }

                if (i != lengthOfCode - 1) {
                    Spacer(modifier = Modifier.width(inputSpacing))
                }
            }
        }

        mutablePincode = pincode
        resetPincodeLiveData()

        if (isError == true && errorText != null) {
            Text(
                text = errorText,
                style = errorLabelTextStyle,
                modifier = Modifier.padding(vertical = errorLabelPaddingVertical)
            )
        }
    }
}

@Composable
private fun DividerWithSpacerStart(
    inputSpacing: Dp = 16f.dp,
    dividerColor: Color = DefaultDividerColor,
    modifier: Modifier
) {
    Spacer(modifier = Modifier.width(inputSpacing))
    Divider(
        color = dividerColor,
        modifier = modifier
    )
}

@Composable
private fun DividerWithSpacerEnd(
    inputSpacing: Dp = 16f.dp,
    dividerColor: Color = DefaultDividerColor,
    modifier: Modifier
) {
    Divider(
        color = dividerColor,
        modifier = modifier
    )
    Spacer(modifier = Modifier.width(inputSpacing))
}

@Composable
@Preview(showBackground = true)
fun ModalPincodeViewPreview() {
    val isError = MutableLiveData<Boolean>()
    val pincode = MutableLiveData<String>()

    PincodeView(
        pincodeLiveData = pincode,
        isErrorLiveData = isError
    )
}