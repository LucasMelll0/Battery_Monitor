package com.example.batterymonitor.other

import com.example.batterymonitor.R

enum class ChargingStatus {
    CHARGING {
        override fun status(): Int = R.string.common_charging
    },
    DISCONNECTED {
        override fun status(): Int = R.string.common_disconnected
    };

    abstract fun status(): Int
}