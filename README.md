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
    implementation "nl.coffeeit.aroma:pincode:1.0.1"
}
```

# 📖 Usage

To use the component as in a Compose project, simply add it to your code as is. This example showcases a simple version:

```
PincodeView(
    pincodeLiveData = pincode,
    isErrorLiveData = isError,
    enableSendButton = true,
    onPincodeCompleted = {
        // Pin code filled in
    },
    onSend = {
        // Send button clicked
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
- `sendButtonTextStyle` - `TextStyle` object to determine the text style of the send button
- `sendButtonDisabledTextStyle` - `TextStyle` object to determine the text style of the error label when disabled
- `onlyDigits` - `Boolean` to make the component accept only digits
- `autoFocusFirstInput` - `Boolean` to make the component focus the first input automatically
- `resetPincodeLiveData` - `Unit` that runs when the Pincode `LiveData` gets reset
- `onBack` - `Unit` that runs when the back button is pressed
- `onPincodeCompleted` - `Unit` that runs when all cells are filled in
- `enableSendButton` - Enables the send button
- `sendButtonConfiguration` - SendButtonConfiguration
  - Example: `SendButtonConfiguration(text = "Send code", cornerShape = RoundedCornerShape(12.Dp), alignment = ButtonPosition.START`
- `sendButtonConfigurationDisabled` - SendButtonConfiguration when  send button is disabled
- `sendCooldownDuration` - integer in seconds, to determine how long the send button should be disabled for when clicked
- `onSend` - `Unit` that runs when the send button is clicked
- `pincodeLiveData` - `LiveData` object of a `String`. This `Object` will be updated to reflect the Pincode characters in the UI.
- `isErrorLiveData` `LiveData` object of a `Boolean`. This `Boolean` determines whether the Pincode view is in an error state or not.
- `sendCodeLiveData` - `LiveData` object of a `Boolean`. When this `Boolean` is set to `true` `onSend` is triggered, 
the timer starts counting down and the send button gets disabled. Should be set to `false` to be able to use it again.
- `keyEventInErrorState` - `Unit` that runs when a key is pressed while the component is in an error state

# ✏️ Changelog

Version 1.0.1 *(18-08-2022)*
----------------------------
* Renamed `resend` methods, variables and constants to `send`.
* Removed `triggerResendOnInit`, a `Boolean` formerly used to decide whether `onSend` should be ran when initializing the component.
* Added `sendCodeLiveData`, a `Boolean` which triggers `onSend`.

Version 1.0.0 *(28-07-2022)*
----------------------------
* Added PincodeView component.

# 📧 Contact
Do you have questions, ideas or need help? Send us an email at contact@coffeeit.nl.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://global-uploads.webflow.com/605a171ee93af49275331843/623b23cdea80a92703e61b42_Logo_black_1.svg" width="100">
  <source media="(prefers-color-scheme: light)" srcset="https://coffeeit.nl/wp-content/uploads/2016/09/logo_dark_small_new.png" width="100">
  <img alt="The Coffee IT logo" src="https://coffeeit.nl/wp-content/uploads/2016/09/logo_dark_small_new.png">
</picture>

# ⚠️ License
Android Aroma Pincode is licensed under the terms of the [MIT Open Source license](https://github.com/Coffee-IT-Development/Pincode-Android-Component/blob/main/LICENSE).