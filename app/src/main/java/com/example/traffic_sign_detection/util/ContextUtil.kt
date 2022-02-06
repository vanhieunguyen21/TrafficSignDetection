package com.example.traffic_sign_detection.util

import android.content.Context

object ContextUtil {
    fun getDrawableResourceId(context: Context, name: String): Int {
        return context.resources.getIdentifier(name, "drawable", context.packageName)
    }
}