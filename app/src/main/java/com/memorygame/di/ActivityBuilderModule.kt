package com.memorygame.di

import com.memorygame.MemoryGameActivity
import com.memorygame.MemoryGameModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MemoryGameModule::class])
    abstract fun bindMemoryGameActivity() : MemoryGameActivity
}