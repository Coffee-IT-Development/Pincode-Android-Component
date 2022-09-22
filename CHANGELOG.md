# Change log

Version 1.0.3 *(22-09-2022)*
----------------------------
* Migrated from `compileSdk` and `targetSdk` 32 to 33.

Version 1.0.2 *(07-09-2022)*
----------------------------
* Removed build variants because it caused issues in other projects.

Version 1.0.1 *(18-08-2022)*
----------------------------
* Renamed `resend` methods, variables and constants to `send`.
* Removed `triggerResendOnInit`, a `Boolean` formerly used to decide whether `onSend` should be ran when initializing the component.
* Added `sendCodeLiveData`, a `Boolean` which triggers `onSend`.

Version 1.0.0 *(28-07-2022)*
----------------------------
* Added PincodeView component.