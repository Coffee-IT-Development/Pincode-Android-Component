package nl.coffeeit.aroma.pincode.presentation

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import nl.coffeeit.aroma.pincode.util.DEFAULT_CORNER_RADIUS

private val DefaultFocusedBorderColor = Color(0xFF6650a4)
private val DefaultUnfocusedBorderColor = Color(0xFF625b71)

@Composable
fun PincodeView(
    lengthOfCode: Int,
    inputCornerShape: RoundedCornerShape = RoundedCornerShape(DEFAULT_CORNER_RADIUS.dp),
    inputColors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = DefaultFocusedBorderColor,
        unfocusedBorderColor = DefaultUnfocusedBorderColor,
        cursorColor = Color.Transparent,
        errorCursorColor = Color.Transparent
    ),
    inputSpacing: Dp = 16f.dp
) {
    Column {
        Row(
            Modifier.fillMaxWidth()
        ) {
            for (i in 0 until lengthOfCode) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {

                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    maxLines = 1,
                    shape = inputCornerShape,
                    colors = inputColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .wrapContentHeight()
                )

                if (i != lengthOfCode - 1) {
                    Spacer(modifier = Modifier.width(inputSpacing))
                }
            }
        }
    }
}
