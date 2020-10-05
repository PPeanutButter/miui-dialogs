package com.peanut.sdk

import android.animation.ValueAnimator
import android.content.Context
import android.view.animation.LinearInterpolator
import com.peanut.sdk.miuidialog.AddInFunction.toast
import com.peanut.sdk.miuidialog.MIUIDialog

fun basicProgress(context: Context) {
    MIUIDialog(context = context).show {
        progress(text = "复制大文件")
        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 10000
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            //给用户反馈
            setProgressText((animation.animatedValue as Int).toString())
            //cancel掉对话框
            if (animation.animatedValue as Int == 100)
                this.cancel()
        }
        onCancel {
            "复制文件完成".toast(context)
        }
        //模拟准备时间
        animator.startDelay = 1000
        animator.start()
    }
}