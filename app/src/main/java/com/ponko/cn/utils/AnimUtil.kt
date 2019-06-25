package com.ponko.cn.utils

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.DecelerateInterpolator

object AnimUtil {
    var animators: HashMap<View, ObjectAnimator> = HashMap()

    /**
     * 抖动动画
     * @param view 需要抖动的view
     * @param shakeFactor 抖动左右旋转幅度
     */
    fun shakeAnim(view: android.view.View?, shakeFactor: Float? = 1f) {
        /**
         * 左右震动效果
         */
        val frame0 = Keyframe.ofFloat(0f, 0f)
        val frame1 = Keyframe.ofFloat(0.1f, -15f)
        val frame2 = Keyframe.ofFloat(0.2f, 15f)
        val frame3 = Keyframe.ofFloat(0.3f, -15f)
        val frame4 = Keyframe.ofFloat(0.4f, 15f)
        val frame5 = Keyframe.ofFloat(0.5f, -15f)
        val frame6 = Keyframe.ofFloat(0.6f, 15f)
        val frame7 = Keyframe.ofFloat(0.7f, -15f)
        val frame8 = Keyframe.ofFloat(0.8f, 15f)
        val frame9 = Keyframe.ofFloat(0.9f, -15f)
        val frame10 = Keyframe.ofFloat(1f, 0f)
        val frameHolder1 = PropertyValuesHolder.ofKeyframe("rotation", frame0, frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8, frame9, frame10)
        /**
         * scaleX放大1.1倍
         */
        val scaleXframe0 = Keyframe.ofFloat(0f, 1f)
        val scaleXframe1 = Keyframe.ofFloat(0.1f, 1.05f)
        val scaleXframe2 = Keyframe.ofFloat(0.2f, 1.05f)
        val scaleXframe3 = Keyframe.ofFloat(0.3f, 1.05f)
        val scaleXframe4 = Keyframe.ofFloat(0.4f, 1.05f)
        val scaleXframe5 = Keyframe.ofFloat(0.5f, 1.05f)
        val scaleXframe6 = Keyframe.ofFloat(0.6f, 1.05f)
        val scaleXframe7 = Keyframe.ofFloat(0.7f, 1.05f)
        val scaleXframe8 = Keyframe.ofFloat(0.8f, 1.05f)
        val scaleXframe9 = Keyframe.ofFloat(0.9f, 1.05f)
        val scaleXframe10 = Keyframe.ofFloat(1f, 1f)
        val frameHolder2 = PropertyValuesHolder.ofKeyframe("ScaleX", scaleXframe0, scaleXframe1, scaleXframe2, scaleXframe3, scaleXframe4, scaleXframe5, scaleXframe6, scaleXframe7, scaleXframe8, scaleXframe9, scaleXframe10)

        /**
         * scaleY放大1.1倍
         */
        val scaleYframe0 = Keyframe.ofFloat(0f, 1f)
        val scaleYframe1 = Keyframe.ofFloat(0.1f, 1.05f)
        val scaleYframe2 = Keyframe.ofFloat(0.2f, 1.05f)
        val scaleYframe3 = Keyframe.ofFloat(0.3f, 1.05f)
        val scaleYframe4 = Keyframe.ofFloat(0.4f, 1.05f)
        val scaleYframe5 = Keyframe.ofFloat(0.5f, 1.05f)
        val scaleYframe6 = Keyframe.ofFloat(0.6f, 1.05f)
        val scaleYframe7 = Keyframe.ofFloat(0.7f, 1.05f)
        val scaleYframe8 = Keyframe.ofFloat(0.8f, 1.05f)
        val scaleYframe9 = Keyframe.ofFloat(0.9f, 1.05f)
        val scaleYframe10 = Keyframe.ofFloat(1f, 1f)
        val frameHolder3 = PropertyValuesHolder.ofKeyframe("ScaleY", scaleYframe0, scaleYframe1, scaleYframe2, scaleYframe3, scaleYframe4, scaleYframe5, scaleYframe6, scaleYframe7, scaleYframe8, scaleYframe9, scaleYframe10)

        /**
         * 构建动画
         */
        val animator = ObjectAnimator.ofPropertyValuesHolder(view, frameHolder1, frameHolder2, frameHolder3)
        animator?.duration = 2000
        animator?.interpolator = DecelerateInterpolator()
        animator?.repeatCount = -1
        animator?.start()
        /**
         * 添加到集合中
         */
        animators[view!!] = animator
    }

    /**
     * 取消动画
     */
    fun cancel(view: View?) {
        if (animators[view] == null)
            return
        if (animators[view]?.isRunning == true) {
            animators[view]?.cancel()
            view?.animate()?.rotation(0f)?.setDuration(100)?.start() //复原状态
        }
    }

    fun cancelAll() {
        for (view in animators) {
            cancel(view.key)
        }
        animators.clear()
    }
}