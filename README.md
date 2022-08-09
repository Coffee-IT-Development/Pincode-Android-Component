![header image](https://coffeeit.nl/wp-content/uploads/2022/07/Aroma_Pincode_Android.png)

# ☕️ Android Aroma Pincode

The Android Aroma Pincode package provides a highly customisable pincode view. This README describes 
how to implement the Pincode component into an app.

This GitHub repository is a mirror, the official repository is hosted privately by Coffee IT.

Created by [Coffee IT](https://coffeeit.nl/). Look at our other repositories on our 
[GitHub account](https://github.com/orgs/Coffee-IT-Development/repositories).

<img src="https://i.imgur.com/PZkGq79.gif" width="300">

# ⚡ Installation

This component requires minimum __SDK 21__.

Add the following to `build.gradle`:
```
dependencies {
    implementation "nl.coffeeit.aroma:pincode:1.0.0"
}
```

# 📖 Usage

To use the component as a Composable, simply add it to your code as is. This example showcases a simple version:

```
PincodeView(
            pincodeLiveData = pincode,
            isErrorLiveData = isError,
            enableResendButton = true,
            onPincodeCompleted = {
                // Pin code filled in
            },
            onResend = {
                // Resend button clicked
            }
        )
```
Two parameters are mandatory to make the Composable work:

`pincodeLiveData` expects a `LiveData` object of a `String`. This object will be updated to reflect the Pincode characters in the UI.

`isErrorLiveData` represents a `LiveData` object of a `Boolean`. This Boolean determines whether the Pincode view is in an error state or not.

## ⚙️ Customisation
__`PincodeView` accepts the following optional parameters for in depth customisation:__

- `lengthOfCode` - An integer that decides the length of the pincode (default 6)
- `inputWidth` - `Dp` object to set width of each input cell
- `inputCornerShape` - `RoundedCornerShape` object to set rounded corners on the input cells
  - Example: `RoundedCornerShape(12.dp)`
- `inputColors` - `TextFieldColors` object to determine the colors in and around the input cells
- `inputSpacing` - `Dp` object to set the spacing betweens each input cell
- `showDividerAfterInput` - decides after how many cells a divider should be shown
- `dividerColor` - the color of the divider
- `dividerHeight` - the height of the divider in `Dp`
- `errorText` - shown text when the component is in an error state
- `errorLabelPaddingVertical` - the vertical padding of the error label in `Dp`
- `focusedBorderThickness` - border thickness of the cells when focused
- `unfocusedBorderThickness` - border thickness of the cells when not focused
- `inputTextStyle` - `TextStyle` object to determine the text style of inputs
- `inputErrorTextStyle` - `TextStyle` object to determine the text style of inputs when in error state
- `errorLabelTextStyle` - `TextStyle` object to determine the text style of the error label
- `resendButtonTextStyle` - `TextStyle` object to determine the text style of the resend button
- `resendButtonDisabledTextStyle` - `TextStyle` object to determine the text style of the error label when disabled
- `onlyDigits` - `Boolean` to make the component accept only digits
- `autoFocusFirstInput` - `Boolean` to make the component focus the first input automatically
- `resetPincodeLiveData` - `Unit` that runs when the Pincode `LiveData` gets reset
- `onBack` - `Unit` that runs when the back button is pressed
- `onPincodeCompleted` - `Unit` that runs when all cells are filled in
- `enableResendButton` - Enables the resend button
- `resendButtonConfiguration` - ResendButtonConfiguration
  - Example: `ResendButtonConfiguration(text = "Send code", cornerShape = RoundedCornerShape(12.Dp), alignment = ButtonPosition.START`
- `resendButtonConfigurationDisabled` - ResendButtonConfiguration when  resend button is disabled
- `resendCooldownDuration` - integer in seconds, to determine how long the resend button should be disabled for when clicked
- `onResend` - `Unit` that runs when the resend button is clicked
- `triggerResendOnInit` - `Boolean` that decides whether `onResend` should be ran when initializing the component
- `keyEventInErrorState` - `Unit` that runs when a key is pressed while the component is in an error state

# ✏️ Change log

Version 1.0.0 *(28-08-2022)*
----------------------------
* Added PincodeView component

# 📧 Contact
Do you have questions, ideas or need help? Send us an email at contact@coffeeit.nl.

 <img src="https://coffeeit.nl/wp-content/uploads/2016/09/logo_dark_small_new.png" width="100">

# ⚠️ License
The Aroma Pincode component is licensed under the terms of the [MIT Open Source license](https://choosealicense.com/licenses/mit/).
