package com.example.batterymonitor.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.batterymonitor.other.BatteryHealth
import com.example.batterymonitor.other.ChargingStatus

class MainViewModel : ViewModel() {

    private val _batteryPercent = MutableLiveData(0)
    internal val batteryPercent: LiveData<Int> = _batteryPercent

    private val _chargingStatus = MutableLiveData(ChargingStatus.DISCONNECTED)
    internal val chargingStatus: LiveData<ChargingStatus> = _chargingStatus

    private val _batteryTechnology = MutableLiveData("")
    internal var batteryTechnology: LiveData<String> = _batteryTechnology

    private val _batteryHealth = MutableLiveData(BatteryHealth.UNKNOWN)
    internal val batteryHealth: LiveData<BatteryHealth> = _batteryHealth

    private val _batteryTemperature = MutableLiveData("")
    internal val batteryTemperature: LiveData<String> = _batteryTemperature

    private val _batteryVoltage = MutableLiveData("")
    internal val batteryVoltage: LiveData<String> = _batteryVoltage

    fun setTechnology(technology: String) {
        _batteryTechnology.postValue(technology)
    }

    fun setHealth(health: BatteryHealth) {
        _batteryHealth.postValue(health)
    }

    fun setTemperature(temperature: String) {
        _batteryTemperature.postValue(temperature)
    }

    fun setVoltage(voltage: String) {
        _batteryVoltage.postValue(voltage)
    }

    fun setBatteryPercent(percent: Int) {
        if (_batteryPercent.value != percent) {
            _batteryPercent.postValue(percent)
        }
    }

    fun setChargingStatus(isCharging: Boolean) {
        if (_batteryPercent.value == 100) {
            _chargingStatus.postValue(ChargingStatus.CHARGED)
        } else {
            _chargingStatus.postValue(if (isCharging) ChargingStatus.CHARGING else ChargingStatus.DISCONNECTED)
        }
    }

}