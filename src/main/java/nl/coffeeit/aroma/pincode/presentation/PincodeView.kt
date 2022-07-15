package nl.coffeeit.aroma.pincode.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val DEFAULT_CORNER_RADIUS = 8
private const val DEFAULT_LENGTH_OF_CODE = 6
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
    isError: Boolean = false,
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
    )
) {
    Column(
        modifier = modifier
    ) {
        var dropDownExpanded by remember { mutableStateOf(false) }

        Row(
            Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { dropDownExpanded = true }
                    )
                }
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
                // TODO: Remove isError check from pincodeCharacter default value, is only for visual check of error state
                var pincodeCharacter by remember { mutableStateOf(if (isError) "1" else "") }
                val interactionSource = remember { MutableInteractionSource() }

                val focusManager = LocalFocusManager.current
                val clipboardManager = LocalClipboardManager.current
                val isLastField = (i == lengthOfCode - 1)
                val isFirstField = (i == 0)

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
                        if (it.key == Key.Backspace && !isFirstField) {
                            focusManager.moveFocus(FocusDirection.Left)
                        }
                        return@onKeyEvent true
                    }

                BasicTextField(
                    value = pincodeCharacter,
                    onValueChange = {
                        if (it.length <= MAXIMUM_AMOUNT_OF_CHARACTERS_PER_INPUT) {
                            pincodeCharacter = it
                            if (it.isNotEmpty() && !isLastField) {
                                focusManager.moveFocus(FocusDirection.Right)
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = if (isLastField) ImeAction.Done else ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Right) },
                        onDone = { focusManager.clearFocus() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    textStyle = if (isError) inputErrorTextStyle else inputTextStyle,
                    interactionSource = interactionSource,
                    cursorBrush = SolidColor(inputColors.cursorColor(isError = isError).value),
                    modifier = cellModifier
                ) { innerTextField ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextFieldDefaults.OutlinedTextFieldDecorationBox(
                            value = pincodeCharacter,
                            visualTransformation = VisualTransformation.None,
                            innerTextField = innerTextField,
                            singleLine = true,
                            enabled = true,
                            contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                                start = 0.dp,
                                end = 0.dp
                            ),
                            isError = isError,
                            interactionSource = interactionSource,
                            border = {
                                TextFieldDefaults.BorderBox(
                                    enabled = true,
                                    isError = isError,
                                    interactionSource = interactionSource,
                                    colors = inputColors,
                                    shape = inputCornerShape,
                                    // TODO: Maybe make border thickness configurable
                                    unfocusedBorderThickness = unfocusedBorderThickness,
                                    focusedBorderThickness = focusedBorderThickness
                                )
                            }
                        )
                    }
                }


                DropdownMenu(
                    expanded = dropDownExpanded,
                    onDismissRequest = { /*TODO*/ }
                ) {
                    DropdownMenuItem(onClick = { dropDownExpanded = false }) {
                        Text("Paste")
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

        if (isError && errorText != null) {
            Text(
                text = errorText,
                style = errorLabelTextStyle,
                modifier = Modifier.padding(vertical = errorLabelPaddingVertical)
            )
        }
    }
}

private fun Modifier.disableClickable(): Modifier =
    composed {
        clickable(
            enabled = false,
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { }
        )
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
    PincodeView()
}
