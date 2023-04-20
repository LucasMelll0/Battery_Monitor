package com.example.batterymonitor.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.BatteryManager.EXTRA_VOLTAGE
import android.util.Log
import android.widget.RemoteViews
import com.example.batterymonitor.R
import com.example.batterymonitor.other.BatteryHealth

/**
 * Implementation of App Widget functionality.
 */
class BatteryWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("Tests", "widget updated")
        for(appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let {
        context.registerReceiver(null, it)
    }
    batteryStatus?.let {
        val voltage = batteryStatus.getIntExtra(EXTRA_VOLTAGE, -1).let {
            String.format("%.1f", (it.toFloat() / 1000))
        }.let {
            context.getString(R.string.battery_voltage_place_holder, it)
        }
        val temperature = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1).let {
            String.format("%.1f", (it.toFloat() / 10))
        }.let {
            context.getString(R.string.battery_temperature_place_holder, it)
        }
        val health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1).let {
            when (it) {
                BatteryManager.BATTERY_HEALTH_COLD -> BatteryHealth.COLD
                BatteryManager.BATTERY_HEALTH_DEAD -> BatteryHealth.DEAD
                BatteryManager.BATTERY_HEALTH_GOOD -> BatteryHealth.GOOD
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> BatteryHealth.OVERHEAT
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> BatteryHealth.OVER_VOLTAGE
                BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> BatteryHealth.UNSPECIFIED_FAILURE
                else -> BatteryHealth.UNKNOWN
            }
        }.let {
            context.getString(it.status())
        }

        val views = RemoteViews(context.packageName, R.layout.battery_widget)
        views.apply {
            setTextViewText(R.id.textview_battery_voltage_widget, voltage)
            setTextViewText(R.id.textview_battery_temperature_widget, temperature)
            setTextViewText(R.id.textview_battery_health_widget, health)
        }
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
    // There may be multiple widgets active, so update all of them
}