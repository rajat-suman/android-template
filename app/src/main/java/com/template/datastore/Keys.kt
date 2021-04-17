package com.template.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey


const val DATA_STORE_NAME = "BaronCustomer"
const val FOREVER_DATA_STORE_NAME = "ForeverBaronCustomer"
val THEME_KEY by lazy { stringPreferencesKey("theme_key") }
val BOOLEAN_DATA by lazy { booleanPreferencesKey("BOOLEAN") }
val LOGIN_DATA by lazy { stringPreferencesKey("LOGIN_DATA") }
val REMEMBER by lazy { booleanPreferencesKey("REMEMBER") }
val LANGUAGE by lazy { stringPreferencesKey("LANGUAGE") }
