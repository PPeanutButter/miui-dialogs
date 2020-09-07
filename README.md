# MIUI-like Dialog

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/17167bea01b548a6bc19d401697a7a69)](https://app.codacy.com/manual/PPeanutButter/miui-dialogs?utm_source=github.com&utm_medium=referral&utm_content=PPeanutButter/miui-dialogs&utm_campaign=Badge_Grade_Dashboard)

> based on [material-dialogs](https://github.com/afollestad/material-dialogs)

入坑这个系列不是为了在MIUI系统上使用特定的(MIUI-dialog)弹窗,而是为了保证App弹窗适配App的UI(毕竟非Material Design的App用系统或者material-dialogs的弹窗总是感觉不搭)

## installation

[![_latestVersion](https://api.bintray.com/packages/ppeanutbutter/maven/miui-dialogs/images/download.svg)](https://bintray.com/ppeanutbutter/maven/miui-dialogs/_latestVersion)

```implementation 'com.peanut.sdk:miui-dialogs:_latestVersion'```

依赖于material-dialogs的core/bottomsheet-3.3.0两个包会自动加入,但是不确定会不会和你额外加入的两个包版本冲突

---

## 一些基础设置
> 都是 [material-dialogs](https://github.com/afollestad/material-dialogs) 的设置

### 夜间主题配置
在您的style.xml中启用的主题加入 `<item name="md_background_color">#222224</item>` （或者其他深色）即可，弹窗会自动判断文字颜色。

### 圆角配置
 在您的style.xml中启用的主题加入 `<item name="md_corner_radius">25dp</item>` 即可，推荐20~30dp。

---

### Basic

Here's a very basic example of creating and showing a dialog:

<img src="https://raw.githubusercontent.com/PPeanutButter/miui-dialogs/master/screen/miui-11-basic.jpg" width="250px" />

```kotlin
MIUIDialog(this).show {
    title(text = "Use Google\'s Location Services?")
    message(text = "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
}
```

### Action Button

There are simple methods for adding action buttons:

<img src="https://raw.githubusercontent.com/PPeanutButter/miui-dialogs/master/screen/miui-11-action-button.jpg" width="250px" />

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
  //onPreShow { dialog -> } 尚未实现
  //onShow { dialog -> } 尚未实现
  onDismiss { dialog -> }
  //onCancel { dialog -> } 尚未实现
}
```

## text-input（输入框）

[Text Input](#text-input)
 1.[Basics](#basics)
 2.[Hints and Prefill](#hints-and-prefill)
 3.[Input Types](#input-types)
 4.[MultiLines](#input-multilines)
 5.[Custom Validation](#custom-validation)

### Basics

You can setup an input dialog using the `input` extension on `MIUIDialog`:

<img src="https://raw.githubusercontent.com/PPeanutButter/miui-dialogs/master/screen/miui-11-input.jpg" width="250px" />

```kotlin
MIUIDialog(this).show {
  input()
  positiveButton(R.string.submit)
}
```

With a setup input dialog, you can retrieve the input field:

```kotlin
val dialog: MIUIDialog = // ...
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

### countdown

> 要求:negative的倒计时结束直接执行negativeAction（用于可能对用户不利的操作，如：获取权限之类的），positive倒计时结束时设置positive按钮可用（用于需要用户三思的危险操作）

### 等待开发
