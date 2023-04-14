package com.example.batterymonitor.ui

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.example.batterymonitor.R
import com.example.batterymonitor.broadcast.BatteryMonitorReceiver
import com.example.batterymonitor.databinding.ActivityMainBinding
import com.example.batterymonitor.other.ChargingStatus
import com.example.batterymonitor.ui.viewmodel.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val monitorReceiver by inject<BatteryMonitorReceiver>()

    private val viewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setsUpBatteryMonitorReceiver()
    }

    override fun onStart() {
        super.onStart()
        setsUpObservers()
    }

    private fun setsUpObservers() {
        viewModel.batteryPercent.observe(this@MainActivity) {
            binding.textviewBatteryPercent.text =
                getString(R.string.battery_percent_place_holder, it)
            binding.progressbarBatteryLevel.progress = it
        }
        viewModel.chargingStatus.observe(this@MainActivity) {
            binding.textviewChargingStatus.text = getString(it.status())
            if (it == ChargingStatus.CHARGING) {
                binding.imageviewBatteryIcon.visibility = View.GONE
                /*binding.animationCharging.playAnimation()*/
            } else {
                binding.imageviewBatteryIcon.visibility = View.VISIBLE
                /*binding.animationCharging.pauseAnimation()*/
            }
        }
        viewModel.batteryIcon.observe(this@MainActivity) {
            binding.imageviewBatteryIcon.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    it.status()
                )
            )
        }
    }

    private fun setsUpBatteryMonitorReceiver() {
        val filter = IntentFilter(Intent.ACTION_POWER_CONNECTED).apply {
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BATTERY_OKAY)
            addAction(Intent.ACTION_BATTERY_CHANGED)
        }
        monitorReceiver.whenPercentChange = {
            viewModel.setBatteryPercent(it)
        }
        monitorReceiver.whenChargingStatusChange = {
            viewModel.setChargingStatus(it)
        }
        registerReceiver(monitorReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(monitorReceiver)
    }
}