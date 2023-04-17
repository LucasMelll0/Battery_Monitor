package com.example.batterymonitor.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager.*
import android.util.Log
import com.example.batterymonitor.model.Battery
import com.example.batterymonitor.other.BatteryHealth

class BatteryMonitorReceiver(
    var whenBatteryUpdate: (battery: Battery) -> Unit = {}
) :
    BroadcastReceiver() {

    companion object {
        const val TAG = "PowerConnectionReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let { batteryStatus ->
            when (batteryStatus.action) {
                Intent.ACTION_BATTERY_CHANGED -> {
                    val status = batteryStatus.getIntExtra(EXTRA_STATUS, -1)
                    val isCharging: Boolean =
                        status == BATTERY_STATUS_CHARGING || status == BATTERY_STATUS_FULL
                    val percent = batteryStatus.let {
                        val level = it.getIntExtra(EXTRA_LEVEL, -1)
                        val scale = it.getIntExtra(EXTRA_SCALE, -1)
                        level * 100 / scale
                    }
                    val technology = batteryStatus.getStringExtra(EXTRA_TECHNOLOGY)
                    val temperature =
                        batteryStatus.getIntExtra(EXTRA_TEMPERATURE, -1).let {
                            String.format("%.1f", (it.toFloat() / 10))
                        }
                    val health = batteryStatus.getIntExtra(EXTRA_HEALTH, -1).let {
                        when (it) {
                            BATTERY_HEALTH_COLD -> BatteryHealth.COLD
                            BATTERY_HEALTH_DEAD -> BatteryHealth.DEAD
                            BATTERY_HEALTH_GOOD -> BatteryHealth.GOOD
                            BATTERY_HEALTH_OVERHEAT -> BatteryHealth.OVERHEAT
                            BATTERY_HEALTH_OVER_VOLTAGE -> BatteryHealth.OVER_VOLTAGE
                            BATTERY_HEALTH_UNSPECIFIED_FAILURE -> BatteryHealth.UNSPECIFIED_FAILURE
                            else -> BatteryHealth.UNKNOWN
                        }
                    }
                    val voltage = batteryStatus.getIntExtra(EXTRA_VOLTAGE, -1).let {
                        String.format("%.1f", (it.toFloat() / 1000))
                    }
                    whenBatteryUpdate(
                        Battery(
                            isCharging = isCharging,
                            percent = percent,
                            technology = technology,
                            temperature = temperature,
                            voltage = voltage,
                            health = health
                        )
                    )
                    Log.d(TAG, "isCharging: $isCharging \n")
                }
                else -> Unit
            }
        }
    }
}