package com.memorygame

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.memorygame.adapters.CardItemAdapter
import com.memorygame.databinding.ActivityMemoryGameBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MemoryGameActivity : DaggerAppCompatActivity(), MemoryGameContract.View {

    @Inject
    lateinit var presenter: MemoryGameContract.Presenter

    @Inject
    lateinit var cardItemAdapter: CardItemAdapter

    private var binding: ActivityMemoryGameBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        setSupportActionBar(binding?.toolbarMemoryGameActivity)
        initRecyclerViewAdapter()
        presenter.startLogic()
    }

    private fun initContentView() {
        binding = ActivityMemoryGameBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
    }

    private fun initRecyclerViewAdapter() {
        binding?.recyclerViewCardItems?.adapter = cardItemAdapter
    }

    override fun notifyAdapter(position: Int) {
        cardItemAdapter.notifyItemChanged(position)
    }

    override fun notifyAdapter() {
        cardItemAdapter.notifyItemRangeChanged(NOTIFY_ITEM_START_RANGE, presenter.getItems().size)
    }

    override fun updateCountDownTimer(second: String) {
        binding?.textViewGameCountDownTimer?.text =
            getString(R.string.label_game_count_down_timer, second)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_memory_game_activity, menu)
        return true
    }

    override fun onWon() {
        binding?.textViewGameCountDownTimer?.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.colorBlack
            )
        )
        binding?.textViewGameCountDownTimer?.text = getString(R.string.label_memory_game_you_won)
    }

    override fun onCriticalTime() {
        binding?.textViewGameCountDownTimer?.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.colorRed
            )
        )
    }

    override fun onTimeFinish() {
        binding?.textViewGameCountDownTimer?.text = getString(R.string.label_memory_game_you_loss)
    }

    private fun resetViews() {
        binding?.textViewGameCountDownTimer?.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.colorBlack
            )
        )
        binding?.textViewGameCountDownTimer?.text = getString(R.string.label_game_start_count_down_timer)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_memory_game_activity_reset_game -> {
                presenter.resetGame()
                resetViews()
            }
        }
        return true
    }

    private companion object {

        private const val NOTIFY_ITEM_START_RANGE = 0
    }
}
