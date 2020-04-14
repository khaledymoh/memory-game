package com.memorygame

import android.os.Handler
import com.memorygame.custom.Timer
import com.memorygame.models.CardItem
import java.util.concurrent.TimeUnit

class MemoryGamePresenter(
    private var view: MemoryGameContract.View?
) : MemoryGameContract.Presenter {

    private var items: MutableList<CardItem> = mutableListOf()

    private var previousItem: CardItem? = null
    private var previousItemPosition: Int? = null
    private var isRunning: Boolean = false
    private var isRestarting = true

    private var gameCountDownTimer = Timer(GAME_COUNT_DOWN_FUTURE, GAME_COUNT_DOWN_INTERVAL)
    private var previewCountDownTimer = Timer(PREVIEW_COUNT_DOWN_FUTURE, PREVIEW_COUNT_DOWN_INTERVAL)

    override fun startLogic() {
        Handler().postDelayed(::startPreview, START_PREVIEW_DELLY)
    }

    override fun onCardItemClick(item: CardItem, position: Int) {
        if (isRestarting) {
            return
        }
        handelCardItemClick(item, position)
        handelWinCase()
    }

    override fun setCardItems(items: MutableList<CardItem>) {
        this.items = items
    }

    private fun handelWinCase() {
        if (items.all { it.isPairFound }) {
            gameCountDownTimer.cancel()
            view?.onWon()
        }
    }

    private fun handelCardItemClick(item: CardItem, position: Int) {
        if (item.isPairFound || item.isOpened) return

        if (!isRunning) {
            item.isOpened = true
            if (item == previousItem) {
                items.filter { it == item }.map { it.isPairFound = true }
                isRunning = false
            } else {
                handelNotPairs(position)
            }

            if (!isRunning) {
                previousItem = item
                previousItemPosition = position
            }

            view?.notifyAdapter(position)
        }
    }

    private fun handelNotPairs(position: Int) {
        if (items.filter { !it.isPairFound && it.isOpened }.size == 2) {
            isRunning = true
            Handler().postDelayed({
                items.filter { !it.isPairFound && it.isOpened }.map { it.isOpened = false }
                view?.notifyAdapter(position)
                previousItemPosition?.let {
                    items[it]
                    view?.notifyAdapter(it)
                }
                resetCurrentRunningItem()
            }, INCORRECT_PAIR_PREVIEW_DELLY)
        } else {
            isRunning = false
        }
    }

    private fun resetCurrentRunningItem() {
        previousItemPosition = null
        previousItem = null
        isRunning = false
    }

    private fun startPreview() {
        items.map { it.isOpened = true }
        view?.notifyAdapter()
        isRestarting = true
        previewCountDownTimer.onFinish {
            items.map { it.isOpened = false }
            view?.notifyAdapter()
            startCountDownTimer()
            isRestarting = false
        }
        previewCountDownTimer.start()
    }

    private fun startCountDownTimer() {
        gameCountDownTimer.onFinish {
            view?.onTimeFinish()
            handelLoss()
        }

        gameCountDownTimer.onTick { millisUntilFinished ->
            var seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished).toString()
            if (seconds.toLong() < 10) {
                seconds = "0$seconds"
                view?.onCriticalTime()
            }

            view?.updateCountDownTimer(seconds)
        }
        gameCountDownTimer.start()
    }

    private fun handelLoss() {
        items.map { it.isOpened = true }
        view?.notifyAdapter()
        isRestarting = true
    }

    override fun resetGame() {
        resetCurrentRunningItem()
        items.map {
            it.isOpened = false
            it.isPairFound = false
        }
        items.shuffle()
        view?.notifyAdapter()
        gameCountDownTimer.cancel()
        startPreview()
    }

    override fun getItems(): MutableList<CardItem> {
        return items
    }

    override fun destroy() {
        view = null
        gameCountDownTimer.cancel()
        previewCountDownTimer.cancel()
    }

    private companion object {

        private const val GAME_COUNT_DOWN_INTERVAL = 1000L
        private const val GAME_COUNT_DOWN_FUTURE = 60000L
        private const val PREVIEW_COUNT_DOWN_FUTURE = 2000L
        private const val PREVIEW_COUNT_DOWN_INTERVAL = 10000L
        private const val INCORRECT_PAIR_PREVIEW_DELLY = 1000L
        private const val START_PREVIEW_DELLY = 1000L
    }
}