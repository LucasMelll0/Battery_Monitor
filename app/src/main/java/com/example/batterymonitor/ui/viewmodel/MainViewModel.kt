package com.example.batterymonitor.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.batterymonitor.other.ChargingStatus

class MainViewModel : ViewModel() {

    private val _batteryPercent = MutableLiveData(0)
    internal val batteryPercent: LiveData<Int> = _batteryPercent

    private val _chargingStatus = MutableLiveData(ChargingStatus.DISCONNECTED)
    internal val chargingStatus: LiveData<ChargingStatus> = _chargingStatus

    fun setBatteryPercent(percent: Int) {
        if (_batteryPercent.value != percent) {
            _batteryPercent.postValue(percent)
        }
    }

    fun setChargingStatus(status: ChargingStatus) {
        if (_chargingStatus.value != status) {
            _chargingStatus.postValue(status)
        }
    }

}