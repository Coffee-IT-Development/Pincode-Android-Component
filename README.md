[![Coffee IT - Android Aroma Pincode Component](https://coffeeit.nl/wp-content/uploads/2022/07/Aroma_Pincode_Android.png)](https://coffeeit.nl/)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/nl.coffeeit.aroma/pincode/badge.svg)](https://maven-badges.herokuapp.com/Maven-Central/nl.coffeeit.aroma/pincode) 
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/license-MIT-brightgreen.svg)](https://github.com/Coffee-IT-Development/Pincode-Android-Component/blob/main/LICENSE)
[![Mirror Repository](https://img.shields.io/badge/Mirror-Repository-9b34eb?style=flat-square)](https://github.com/Coffee-IT-Development/Pincode-Android-Component)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-@CoffeeIT-blue.svg?style=flat-square)](https://linkedin.com/company/coffee-it)
[![Facebook](https://img.shields.io/badge/Facebook-CoffeeITNL-blue.svg?style=flat-square)](https://www.facebook.com/CoffeeITNL/)
[![Instagram](https://img.shields.io/badge/Instagram-CoffeeITNL-blue.svg?style=flat-square)](https://www.instagram.com/coffeeitnl/)
[![Follow coffeeitnl on Twitter](https://img.shields.io/twitter/follow/coffeeitnl.svg?style=flat-square&logo=twitter)](https://twitter.com/coffeeitnl)

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

To use the component as in a Compose project, simply add it to your code as is. This example showcases a simple version:

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

To use the component as part of a XML-based project, add a `ComposeView` element to your layout file and call the `setContent` method in your activity/fragment:

```
lateinit var binding: ActivityMainBinding
    private val isError = MutableLiveData(false)
    private val pincode = MutableLiveData("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.cvPincode.setContent {
            PincodeView(pincodeLiveData = pincode, isErrorLiveData = isError)
        }
    }
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

# ✏️ Changelog

Version 1.0.0 *(28-07-2022)*
----------------------------
* Added PincodeView component

# 📧 Contact
Do you have questions, ideas or need help? Send us an email at contact@coffeeit.nl.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://global-uploads.webflow.com/605a171ee93af49275331843/623b23cdea80a92703e61b42_Logo_black_1.svg" width="100">
  <source media="(prefers-color-scheme: light)" srcset="https://coffeeit.nl/wp-content/uploads/2016/09/logo_dark_small_new.png" width="100">
  <img alt="The Coffee IT logo" src="https://coffeeit.nl/wp-content/uploads/2016/09/logo_dark_small_new.png">
</picture>

# ⚠️ License
The Aroma Pincode component is licensed under the terms of the [MIT Open Source license](https://github.com/Coffee-IT-Development/Pincode-Android-Component/blob/main/LICENSE).