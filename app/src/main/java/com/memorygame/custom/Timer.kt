package com.memorygame.custom

import android.os.CountDownTimer

class Timer(future: Long, interval: Long) : CountDownTimer(future, interval) {

    private var onFinishListener: (() -> Unit)? = null
    private var onTickListener: ((millisUntilFinished: Long) -> Unit)? = null

    override fun onFinish() {
        onFinishListener?.invoke()
    }

    override fun onTick(millisUntilFinished: Long) {
        onTickListener?.invoke(millisUntilFinished)
    }

    fun onFinish(onFinishListener: (() -> Unit)) {
        this.onFinishListener = onFinishListener
    }

    fun onTick(onTickListener: ((millisUntilFinished: Long) -> Unit)) {
        this.onTickListener = onTickListener
    }
}