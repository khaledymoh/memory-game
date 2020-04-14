package com.memorygame.adapters

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.memorygame.MemoryGameContract
import com.memorygame.models.CardItem
import com.memorygame.R

class CardItemAdapter(
    private var presenter: MemoryGameContract.Presenter,
    private var items: MutableList<CardItem>
) : RecyclerView.Adapter<CardItemAdapter.CardItemViewHolder>() {

    private lateinit var context: Context

    init {
        items.shuffle()
        presenter.setCardItems(items)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardItemViewHolder {
        return CardItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_card_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardItemViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    inner class CardItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var cardViewCardItemRoot: MaterialCardView? = null
        private var imageViewCardIcon: AppCompatImageView? = null
        private var textViewCardTitle: MaterialTextView? = null

        init {
            findViews()
        }

        fun bind(item: CardItem, position: Int) {
            itemView.setOnClickListener {
                presenter.onCardItemClick(item, position)
            }
            handelItemView(item)
        }

        private fun findViews() {
            cardViewCardItemRoot = itemView.findViewById(R.id.card_view_root)
            imageViewCardIcon = itemView.findViewById(R.id.image_view_card_icon)
            textViewCardTitle = itemView.findViewById(R.id.text_view_card_title)
        }

        private fun handelItemView(item: CardItem) {
            item.icon?.let { imageViewCardIcon?.setImageResource(it) }
            if (item.isOpened) {
                flipCardAnimation()
            } else {
                reverseFlipCardAnimation()
            }
        }

        private fun flipCardAnimation() {
            val rotationYObjectAnimator =
                getRotationYObjectAnimator(
                    itemView,
                    START_ANIMATION_VALUE
                )
            rotationYObjectAnimator.doOnEnd {
                cardViewCardItemRoot?.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorAccent
                    )
                )
                imageViewCardIcon?.visibility = View.VISIBLE
                textViewCardTitle?.visibility = View.GONE
            }
            rotationYObjectAnimator.start()
        }

        private fun reverseFlipCardAnimation() {
            val rotationYObjectAnimator =
                getRotationYObjectAnimator(
                    itemView,
                    EXIT_START_ANIMATION_VALUE
                )
            rotationYObjectAnimator.doOnEnd {
                cardViewCardItemRoot?.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorDarkGray
                    )
                )
                imageViewCardIcon?.visibility = View.GONE
                textViewCardTitle?.visibility = View.VISIBLE
            }
            rotationYObjectAnimator.start()
        }

        private fun getRotationYObjectAnimator(
            view: View,
            startValue: Float,
            endValue: Float = END_ANIMATION_VALUE
        ): ObjectAnimator {
            val enterRotationYObjectAnimator =
                ObjectAnimator.ofFloat(
                    view,
                    ANIMATION_PROPERTY_NAME, startValue, endValue
                ).apply {
                    duration =
                        ANIMATION_DURATION
                }
            enterRotationYObjectAnimator.interpolator = AccelerateDecelerateInterpolator()

            return enterRotationYObjectAnimator
        }
    }

    private companion object {

        private const val START_ANIMATION_VALUE = 180f
        private const val EXIT_START_ANIMATION_VALUE = -180f
        private const val END_ANIMATION_VALUE = 0f
        private const val ANIMATION_PROPERTY_NAME = "rotationY"
        private const val ANIMATION_DURATION = 250L
    }
}