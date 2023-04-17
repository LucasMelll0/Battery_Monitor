package com.example.batterymonitor.extensions

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat

/* return a drawable from your res id */
fun Activity.findDrawable(@DrawableRes resId: Int) =
    ResourcesCompat.getDrawable(resources, resId, null)