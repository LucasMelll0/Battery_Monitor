package com.example.batterymonitor.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import com.example.batterymonitor.other.ChargingStatus

class BatteryMonitorReceiver(
    var whenPercentChange: (percent: Int) -> Unit = {},
    var whenChargingStatusChange: (chargingStatus: ChargingStatus) -> Unit = {}
) :
    BroadcastReceiver() {

    companion object {
        const val TAG = "PowerConnectionReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let { batteryStatus ->
            when (batteryStatus.action) {
                Intent.ACTION_BATTERY_CHANGED -> {
                    val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                    val isCharging: Boolean =
                        status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
                    val batteryPercent = batteryStatus.let {
                        val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                        val scale = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                        level * 100 / scale
                    }
                    whenPercentChange(batteryPercent)
                    whenChargingStatusChange(if (isCharging) ChargingStatus.CHARGING else ChargingStatus.DISCONNECTED)
                    Log.d(TAG, "isCharging: $isCharging \n")
                    Log.d(TAG, "battery status: $batteryPercent")
                }
                Intent.ACTION_BATTERY_OKAY -> {
                    Log.d(TAG, "battery ok")
                }
                Intent.ACTION_BATTERY_LOW -> {
                    Log.d(TAG, "battery low")
                }
                else -> Unit
            }
        }
    }
}