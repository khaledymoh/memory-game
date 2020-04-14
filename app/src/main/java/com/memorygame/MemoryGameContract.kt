package com.memorygame

import com.memorygame.models.CardItem

interface MemoryGameContract {

    interface Presenter {

        fun startLogic()

        fun onCardItemClick(item: CardItem, position: Int)

        fun setCardItems(items: MutableList<CardItem>)

        fun resetGame()

        fun getItems() : MutableList<CardItem>

        fun destroy()
    }

    interface View {

        fun notifyAdapter(position: Int)

        fun notifyAdapter()

        fun updateCountDownTimer(second: String)

        fun onCriticalTime()

        fun onTimeFinish()

        fun onWon()
    }
}