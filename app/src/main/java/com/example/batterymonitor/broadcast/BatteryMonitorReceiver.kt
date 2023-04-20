package com.example.batterymonitor.broadcast

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.BatteryManager.BATTERY_HEALTH_COLD
import android.os.BatteryManager.BATTERY_HEALTH_DEAD
import android.os.BatteryManager.BATTERY_HEALTH_GOOD
import android.os.BatteryManager.BATTERY_HEALTH_OVERHEAT
import android.os.BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE
import android.os.BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE
import android.os.BatteryManager.BATTERY_STATUS_CHARGING
import android.os.BatteryManager.BATTERY_STATUS_FULL
import android.os.BatteryManager.EXTRA_HEALTH
import android.os.BatteryManager.EXTRA_LEVEL
import android.os.BatteryManager.EXTRA_SCALE
import android.os.BatteryManager.EXTRA_STATUS
import android.os.BatteryManager.EXTRA_TECHNOLOGY
import android.os.BatteryManager.EXTRA_TEMPERATURE
import android.os.BatteryManager.EXTRA_VOLTAGE
import android.os.PowerManager
import android.os.PowerManager.ACTION_POWER_SAVE_MODE_CHANGED
import android.util.Log
import android.widget.RemoteViews
import com.example.batterymonitor.R
import com.example.batterymonitor.model.Battery
import com.example.batterymonitor.other.BatteryHealth
import com.example.batterymonitor.widget.BatteryWidget

class BatteryMonitorReceiver(
    var whenBatteryUpdate: (battery: Battery) -> Unit = {},
    var whenPowerSaveModeChanged: (enabled: Boolean) -> Unit = {}
) :
    BroadcastReceiver() {

    companion object {
        const val TAG = "PowerConnectionReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let { batteryStatus ->
            when (batteryStatus.action) {
                Intent.ACTION_BATTERY_CHANGED -> {
                    changeBatteryInfo(batteryStatus)
                    updateWidget(context!!, batteryStatus)
                }

                ACTION_POWER_SAVE_MODE_CHANGED -> {
                    context?.let {
                        val powerManager: PowerManager =
                            context.getSystemService(Context.POWER_SERVICE) as PowerManager
                        whenPowerSaveModeChanged(powerManager.isPowerSaveMode)
                    }

                }

                else -> Unit
            }
        }
    }
    private fun updateWidget(context: Context, batteryStatus: Intent) {
        val percent = batteryStatus.let {
            val level = it.getIntExtra(EXTRA_LEVEL, -1)
            val scale = it.getIntExtra(EXTRA_SCALE, -1)
            level * 100 / scale
        }.let {
            context.getString(R.string.battery_percent_place_holder, it)
        }
        val temperature = batteryStatus.getIntExtra(EXTRA_TEMPERATURE, -1).let {
            String.format("%.1f", (it.toFloat() / 10))
        }.let {
            context.getString(R.string.battery_temperature_place_holder, it)
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
        }.let {
            context.getString(it.status())
        }
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetsIds = appWidgetManager.getAppWidgetIds(
            ComponentName(
                context,
                BatteryWidget::class.java
            )
        )
        val views = RemoteViews(context.packageName, R.layout.battery_widget)
        views.apply {
            setTextViewText(R.id.textview_battery_percent_widget, percent)
            setTextViewText(R.id.textview_battery_temperature_widget, temperature)
            setTextViewText(R.id.textview_battery_health_widget, health)
        }
        widgetsIds.forEach {
            appWidgetManager.updateAppWidget(it, views)
            Log.d(TAG, "widgets updated")
        }
    }

    private fun changeBatteryInfo(batteryStatus: Intent) {
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
    }
}