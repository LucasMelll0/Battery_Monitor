package com.example.batterymonitor.di

import com.example.batterymonitor.broadcast.BatteryMonitorReceiver
import com.example.batterymonitor.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
    viewModel { MainViewModel() }
    single { BatteryMonitorReceiver() }
}