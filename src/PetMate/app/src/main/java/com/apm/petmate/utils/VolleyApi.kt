package com.apm.petmate.utils

import android.app.Activity
import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleyApi constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: VolleyApi? = null

        fun getInstance(context: Activity) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleyApi(context).also {
                    INSTANCE = it
                }
            }
    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}