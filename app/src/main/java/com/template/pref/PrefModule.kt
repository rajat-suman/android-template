package com.template.pref

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class PrefModule {
    @Provides
    @Singleton
    fun getPref(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun getEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor =
        sharedPreferences.edit()

}