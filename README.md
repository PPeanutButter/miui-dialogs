# MIUI-like Dialog
> based on [material-dialogs](https://github.com/afollestad/material-dialogs)

入坑这个系列不是为了在MIUI系统上使用特定的(MIUI-dialog)弹窗,而是为了保证App弹窗适配App的UI(毕竟非Material Design的App用系统或者material-dialogs的弹窗总是感觉不搭)

## installation
[ ![_latestVersion](https://api.bintray.com/packages/ppeanutbutter/maven/miui-dialogs/images/download.svg) ](https://bintray.com/ppeanutbutter/maven/miui-dialogs/_latestVersion)

```implementation 'com.peanut.sdk:miui-dialogs:_latestVersion'```

依赖于material-dialogs的core/bottomsheet-3.3.0两个包会自动加入,但是不确定会不会和你额外加入的两个包版本冲突

---
## 一些基础设置
> 都是 [material-dialogs](https://github.com/afollestad/material-dialogs) 的设置

### 夜间主题
在您的style.xml中启用的主题加入 `<item name="md_background_color">#222224</item>` （或者其他深色）即可，弹窗会自动判断文字颜色。

### 圆角
 在您的style.xml中启用的主题加入 `<item name="md_corner_radius">15dp</item>` 即可，推荐15~20dp。

## MIUI-11系列
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
### input
### countdown
> 要求:negative的倒计时结束直接执行negativeAction（用于可能对用户不利的操作，如：获取权限之类的），positive倒计时结束时设置positive按钮可用（用于需要用户三思的危险操作）

### 等待开发
## MIUI-12系列
### 等待开发
