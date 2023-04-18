package com.example.batterymonitor.other

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

enum class BatteryHealth {
    COLD {
        override fun status(): Int = R.string.common_cold
    },
    DEAD {
        override fun status(): Int = R.string.common_dead
    },
    GOOD {
        override fun status(): Int = R.string.common_good
    },
    OVERHEAT {
        override fun status(): Int = R.string.common_overheat
    },
    OVER_VOLTAGE {
        override fun status(): Int = R.string.common_over_voltage
    },
    UNKNOWN {
        override fun status(): Int = R.string.common_unknown
    },
    UNSPECIFIED_FAILURE {
        override fun status(): Int = R.string.common_unspecified_failure
    };

    @StringRes
    abstract fun status(): Int
}