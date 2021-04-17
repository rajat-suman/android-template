package com.template.networkcalls

import android.util.Log
import androidx.collection.LruCache
import retrofit2.Response

class CacheUtil : LruCache<ApiEnums, Response<Any>>(1024) {

    override fun entryRemoved(
        evicted: Boolean,
        key: ApiEnums,
        oldValue: Response<Any>,
        newValue: Response<Any>?
    ) {
        super.entryRemoved(evicted, key, oldValue, newValue)
        Log.d("entryRemoved", "entryRemoved")
    }

    override fun sizeOf(key: ApiEnums, value: Response<Any>): Int {
        Log.d("sizeOf", "sizeOf")
        return super.sizeOf(key, value)
    }

}