# MIUI-like Dialog

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/17167bea01b548a6bc19d401697a7a69)](https://app.codacy.com/manual/PPeanutButter/miui-dialogs?utm_source=github.com&utm_medium=referral&utm_content=PPeanutButter/miui-dialogs&utm_campaign=Badge_Grade_Dashboard)  [![](https://img.shields.io/badge/镜像站-Fastgit-1bcc1b)](https://hub.fastgit.org/PPeanutButter/miui-dialogs)  ![Bintray](https://img.shields.io/bintray/dt/ppeanutbutter/maven/miui-dialogs?color=1bcc1b)  ![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/ppeanutbutter/miui-dialogs?color=1bcc1b)  ![GitHub top language](https://img.shields.io/github/languages/top/ppeanutbutter/miui-dialogs?color=1bcc1b&label=Kotlin)

> based on [material-dialogs](https://github.com/afollestad/material-dialogs)

入坑这个系列不是为了在MIUI系统上使用特定的(MIUI-dialog)弹窗,而是为了保证App弹窗适配App的UI(毕竟非Material Design的App用系统或者material-dialogs的弹窗总是感觉不搭)

## installation

![Bintray](https://img.shields.io/bintray/v/ppeanutbutter/maven/miui-dialogs?color=1bcc1b&label=dialog-version)

```implementation 'com.peanut.sdk:miui-dialogs:dialog-version'```

![Bintray](https://img.shields.io/bintray/v/drummer-aidan/maven/material-dialogs:core?color=1bcc1b&label=core_version)

```implementation 'com.afollestad.material-dialogs:core:core_version'```

![Bintray](https://img.shields.io/bintray/v/drummer-aidan/maven/material-dialogs:bottomsheets?color=1bcc1b&label=bottomsheets_version)

```implementation 'com.afollestad.material-dialogs:bottomsheets:bottomsheets_version'```

---

## Basic Dialog Settings
> all settings belongs to material-dialogs, [see all](https://github.com/afollestad/material-dialogs)

### night-mode
Adding code `<item name="md_background_color">#222224</item>` to your `style.xml`。

### corner
Adding code `<item name="md_corner_radius">25dp</item>` to your `style.xml` (recommend 20~30dp)。

---

### Basic

Here's a very basic example of creating and showing a dialog:

<img src="https://raw.githubusercontent.com/PPeanutButter/miui-dialogs/master/screen/miui-11-basic.png" width="400px" />

```kotlin
MIUIDialog(this).show {
    title(text = "Use Google\'s Location Services?")
    message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
}
```

### Action Button

There are simple methods for adding action buttons:

<img src="https://raw.githubusercontent.com/PPeanutButter/miui-dialogs/master/screen/miui-11-action-button.png" width="400px" />

```kotlin
MIUIDialog(this).show {
    title(text = "Use Google\'s Location Services?")
    message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
    positiveButton(text = "Agree"){
        //do
    }
    negativeButton(text = "Disagree"){
        //do
    }
}
```

### Callbacks

There are a few lifecycle callbacks you can hook into:

```kotlin
MIUIDialog(this).show {
  //onPreShow { dialog -> } not implemented yet!
  //onShow { dialog -> } not implemented yet!
  onDismiss { dialog -> }
  //onCancel { dialog -> } not implemented yet!
}
```

### CountDown

You can disable Positive Button in first few seconds(let user wait):

<img src="https://raw.githubusercontent.com/PPeanutButter/miui-dialogs/master/screen/miui-11-positive-countdown.png" width="400px" />

```kotlin
MIUIDialog(this).show {
    title(text = "Use Google\'s Location Services?")
    message(text = "Let Google help apps determine location. This means sending")
    positiveButton(text = "Agree",countdown = 5){
        "you clicked positive button!".toast(this@MainActivity3)
    }
    negativeButton(text = "Disagree"){
        "you clicked negative button!".toast(this@MainActivity3)
    }
}
```

---

You can also set run Negative Callback automatically when wait time-out:

<img src="https://raw.githubusercontent.com/PPeanutButter/miui-dialogs/master/screen/miui-11-negative-countdown.png" width="400px" />

```kotlin
MIUIDialog(this).show {
    title(text = "Use Google\'s Location Services?")
    message(text = "Let Google help apps determine location. This means sending a")
    positiveButton(text = "Agree"){
        "you clicked positive button!".toast(this@MainActivity3)
    }
    negativeButton(text = "Disagree",countdown = 10){
        "you clicked negative button!".toast(this@MainActivity3)
    }
}
```

## text-input

1. [Text Input](#text-input)
    1. [Basics](#basics)
    2. [Hints and Prefill](#hints-and-prefill)
    3. [Input Types](#input-types)
    4. [MultiLines](#input-multilines)
    5. [Custom Validation](#custom-validation)

### Basics

You can setup an input dialog using the `input` extension on `MIUIDialog`:

<img src="https://raw.githubusercontent.com/PPeanutButter/miui-dialogs/master/screen/miui-11-input.png" width="400px" />

```kotlin
MIUIDialog(this).show {
  input()
  positiveButton(R.string.submit)
}
```

With a setup input dialog, you can retrieve the input field:

```kotlin
val dialog: MIUIDialog = MIUIDialog(this).show{}
val inputField: EditText = dialog.getInputField()
```

---

You can append a lambda to receive a callback when the positive action button is pressed with 
text entered: 

```kotlin
MIUIDialog(this).show {
  input { dialog, text ->
      // Text submitted with the action button
  }
  positiveButton(R.string.submit)
}
```

If you set `waitForPositiveButton` to false, the callback is invoked every time the text field is
modified:

```kotlin
MIUIDialog(this).show {
  input(waitForPositiveButton = false) { dialog, text ->
      // Text changed
  }
  positiveButton(R.string.done)
}
```

To allow the positive action button to be pressed even when the input is empty:

```kotlin
MIUIDialog(this).show {
  input(allowEmpty = true) { dialog, text ->
      // Text submitted with the action button, might be an empty string`
  }
  positiveButton(R.string.done)
}
```

### Hints and Prefill

You can set a hint to the input field, which is the gray faded text shown when the field is empty:

```kotlin
MIUIDialog(this).show {
  input(hintRes = R.string.hint_text)
}
```

A literal string can be used as well:

```kotlin
MIUIDialog(this).show {
  input(hint = "Your Hint Text")
}
```

---

You can also prefill the input field:

```kotlin
MIUIDialog(this).show {
  input(prefillRes = R.string.prefill_text)
}
```

A literal string can be used as well:

```kotlin
MIUIDialog(this).show {
  input(prefill = "Prefilled text")
}
```

### Input Types

You can apply input types to the input field, which modifies the keyboard type when the field is 
focused on. This is just taken right from the Android framework, the input type gets applied 
directly to the underlying `EditText`:

```kotlin
val type = InputType.TYPE_CLASS_TEXT or 
  InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
  
MIUIDialog(this).show {
  input(inputType = type)
}
```

### input-multilines

By default, input lines is 1, you can set `multiLines = true` to activate multilines input when input content maght be large

```kotlin
MIUIDialog(this).show {
    title(text = "Use Google\'s Location Services?")
    input(hint = "Type something", multiLines = true){ charSequence, _ ->
        charSequence?.toString()?.toast(this@MainActivity3)
    }
    positiveButton(text = "Agree")
    negativeButton(text = "Disagree"){
        "You clicked negative button!".toast(this@MainActivity3)
    }
}
```

### Custom Validation

You can do custom validation using the input listener. This example enforces that the input
starts with the letter 'a':

```kotlin
MIUIDialog(this).show {
  input(waitForPositiveButton = false) { dialog, text ->
    val inputField = dialog.getInputField()
    val isValid = text.startsWith("a", true)
    
    inputField?.error = if (isValid) null else "Must start with an 'a'!"
    dialog.setActionButtonEnabled(POSITIVE, isValid)
  }
  positiveButton(R.string.submit)
}
```

### to do...
