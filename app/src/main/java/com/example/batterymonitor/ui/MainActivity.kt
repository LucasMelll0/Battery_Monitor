package com.example.batterymonitor.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PowerManager
import android.os.PowerManager.ACTION_POWER_SAVE_MODE_CHANGED
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.batterymonitor.R
import com.example.batterymonitor.broadcast.BatteryMonitorReceiver
import com.example.batterymonitor.databinding.ActivityMainBinding
import com.example.batterymonitor.extensions.findDrawable
import com.example.batterymonitor.other.ChargingStatus
import com.example.batterymonitor.ui.bottomsheet.BatteryDetailsBottomSheet
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
        setsUpButtonBatteryDetails()
    }

    private fun setsUpButtonBatteryDetails() {
        binding.buttonBatteryDetails.setOnClickListener {
            setsUpBottomSheetBatteryDetails()
        }
    }

    private fun setsUpBottomSheetBatteryDetails() {
        val bottomSheetBatteryDetails = BatteryDetailsBottomSheet()
        bottomSheetBatteryDetails.show(supportFragmentManager, BatteryDetailsBottomSheet.TAG)
    }

    private fun setsUpObservers() {
        viewModel.batteryPercent.observe(this@MainActivity) {
            updatePercent(it)
        }
        viewModel.chargingStatus.observe(this@MainActivity) {
            binding.textviewChargingStatus.text = getString(it.status())
            if (it == ChargingStatus.CHARGING) {
                binding.animationViewCharging?.visibility = View.VISIBLE
            } else {
                binding.animationViewCharging?.visibility = View.INVISIBLE
            }
        }
    }

    private fun updatePercent(percent: Int) {
        binding.textviewBatteryPercent.text =
            getString(R.string.battery_percent_place_holder, percent)
        val progressBar = binding.progressbarBatteryLevel
        updateProgressBar(progressBar, percent)
    }

    private fun updateProgressBar(progressBar: ProgressBar, percent: Int) {
        val bounds = progressBar.progressDrawable.bounds
        progressBar.progressDrawable = when (percent) {
            in 35..100 -> findDrawable(R.drawable.progress_bar_battery_level_default)
            in 25..35 -> findDrawable(R.drawable.progress_bar_battery_level_medium)
            in 0..34 -> findDrawable(R.drawable.progress_bar_battery_level_low)
            else -> findDrawable(R.drawable.progress_bar_battery_level_default)
        }
        progressBar.progressDrawable.bounds = bounds
        progressBar.progress = percent
    }

    private fun setsUpBatteryMonitorReceiver() {
        togglePowerSaveModeWarningVisibility(
            (getSystemService(Context.POWER_SERVICE) as PowerManager).isPowerSaveMode
        )
        val filter = IntentFilter(Intent.ACTION_POWER_CONNECTED).apply {
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(ACTION_POWER_SAVE_MODE_CHANGED)
        }
        monitorReceiver.whenBatteryUpdate = {
            Log.d("Tests", "setsUpBatteryMonitorReceiver: $it")
            viewModel.setBatteryInfo(it)
        }
        monitorReceiver.whenPowerSaveModeChanged = {
            togglePowerSaveModeWarningVisibility(it)
        }
        registerReceiver(monitorReceiver, filter)
    }

    private fun togglePowerSaveModeWarningVisibility(enabled: Boolean) {
        val textViewPowerSaveWarning = binding.textviewWarning
        if (enabled && textViewPowerSaveWarning.visibility != View.VISIBLE) {
            val enterAnim = AnimationUtils.loadAnimation(this, R.anim.enter_from_top)
            textViewPowerSaveWarning.startAnimation(enterAnim)
        }else {
            val exitAnim = AnimationUtils.loadAnimation(this, R.anim.exit_to_top)
            textViewPowerSaveWarning.startAnimation(exitAnim)
        }
        textViewPowerSaveWarning.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(monitorReceiver)
    }
}