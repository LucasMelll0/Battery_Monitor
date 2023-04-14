package com.example.batterymonitor.other

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.batterymonitor.R


enum class ChargingStatus {
    CHARGING {
        override fun status(): Int = R.string.common_charging
    },
    DISCONNECTED {
        override fun status(): Int = R.string.common_disconnected
    },
    CHARGED {
        override fun status(): Int = R.string.common_charged
    };

    @StringRes
    abstract fun status(): Int
}

enum class BatteryIcon {
    FULL {
        override fun status(): Int = R.drawable.ic_battery_full
    },
    HIGH {
        override fun status(): Int = R.drawable.ic_battery_high
    },
    MEDIUM {
        override fun status(): Int = R.drawable.ic_battery_medium
    },
    LOW {
        override fun status(): Int = R.drawable.ic_battery_low
    };

    @DrawableRes
    abstract fun status(): Int
}