package com.example.batterymonitor.model

import com.example.batterymonitor.other.BatteryHealth

class Battery(
    val isCharging: Boolean,
    val percent: Int,
    val technology: String?,
    val temperature: String,
    val voltage: String,
    val health: BatteryHealth
) {
    override fun toString(): String {
        buildString {
            append("Battery Information")
            append("isCharging: $isCharging \n")
            append("percent: $percent \n")
            append("technology: $technology \n")
            append("temperature: $temperature \n")
            append("voltage: $voltage \n")
            append("health: $health")
        }.also {
            return it
        }
    }
}