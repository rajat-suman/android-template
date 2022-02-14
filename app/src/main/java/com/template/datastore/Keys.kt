package com.template.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore


const val DATA_STORE_NAME = "ApplicationTemplate"
const val FOREVER_DATA_STORE_NAME = "ForeverApplicationTemplate"
val THEME_KEY by lazy { stringPreferencesKey("theme_key") }
val BOOLEAN_DATA by lazy { booleanPreferencesKey("BOOLEAN") }
val LOGIN_DATA by lazy { stringPreferencesKey("LOGIN_DATA") }
val REMEMBER by lazy { booleanPreferencesKey("REMEMBER") }
val LANGUAGE by lazy { stringPreferencesKey("LANGUAGE") }

val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)
