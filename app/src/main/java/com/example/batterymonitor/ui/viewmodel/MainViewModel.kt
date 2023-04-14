package com.example.batterymonitor.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.batterymonitor.other.BatteryIcon
import com.example.batterymonitor.other.ChargingStatus

class MainViewModel : ViewModel() {

    private val _batteryPercent = MutableLiveData(0)
    internal val batteryPercent: LiveData<Int> = _batteryPercent

    private val _chargingStatus = MutableLiveData(ChargingStatus.DISCONNECTED)
    internal val chargingStatus: LiveData<ChargingStatus> = _chargingStatus

    private val _batteryIcon = MutableLiveData(BatteryIcon.FULL)
    internal val batteryIcon: LiveData<BatteryIcon> = _batteryIcon

    fun setBatteryPercent(percent: Int) {
        if (_batteryPercent.value != percent) {
            _batteryPercent.postValue(percent)
            setBatteryIcon(percent)
        }
    }

    private fun setBatteryIcon(percent: Int) {
        val batteryIcon = when (percent) {
            in 95..100 -> {
                BatteryIcon.FULL
            }
            in 60..94 -> {
                BatteryIcon.HIGH
            }
            in 30..59 -> {
                BatteryIcon.MEDIUM
            }
            else -> {
                BatteryIcon.LOW
            }
        }
        _batteryIcon.postValue(batteryIcon)
    }

    fun setChargingStatus(isCharging: Boolean) {
        if (_batteryPercent.value == 100) {
            _chargingStatus.postValue(ChargingStatus.CHARGED)
        }else {
            _chargingStatus.postValue(if (isCharging) ChargingStatus.CHARGING else ChargingStatus.DISCONNECTED)
        }
    }

}