package com.memorygame

import com.memorygame.adapters.CardItemAdapter
import com.memorygame.di.ActivityScope
import com.memorygame.models.CardItem
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object MemoryGameModule {

    @JvmStatic
    @ActivityScope
    @Provides
    fun providePresenter(view: MemoryGameActivity): MemoryGameContract.Presenter {
        return MemoryGamePresenter(view)
    }

    @JvmStatic
    @Provides
    fun provideCardAdapter(
        presenter: MemoryGameContract.Presenter,
        items: MutableList<CardItem>
    ): CardItemAdapter {
        return CardItemAdapter(presenter, items)
    }

    @JvmStatic
    @Provides
    fun provideCardAdapterItems(): MutableList<CardItem> {
        val items: MutableList<CardItem> = mutableListOf()
        items.add(CardItem(icon = R.drawable.ic_github_logo))
        items.add(CardItem(icon = R.drawable.ic_github_logo))
        items.add(CardItem(icon = R.drawable.ic_microsoft_logo))
        items.add(CardItem(icon = R.drawable.ic_microsoft_logo))
        items.add(CardItem(icon = R.drawable.ic_facebook_logo))
        items.add(CardItem(icon = R.drawable.ic_facebook_logo))
        items.add(CardItem(icon = R.drawable.ic_android_logo))
        items.add(CardItem(icon = R.drawable.ic_android_logo))
        items.add(CardItem(icon = R.drawable.ic_appel_logo))
        items.add(CardItem(icon = R.drawable.ic_appel_logo))
        items.add(CardItem(icon = R.drawable.ic_windows_logo))
        items.add(CardItem(icon = R.drawable.ic_windows_logo))
        items.add(CardItem(icon = R.drawable.ic_ios_logo))
        items.add(CardItem(icon = R.drawable.ic_ios_logo))
        items.add(CardItem(icon = R.drawable.ic_uber_logo))
        items.add(CardItem(icon = R.drawable.ic_uber_logo))
        return items
    }
}