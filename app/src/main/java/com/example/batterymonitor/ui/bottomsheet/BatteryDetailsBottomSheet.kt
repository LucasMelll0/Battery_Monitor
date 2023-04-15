package com.example.batterymonitor.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.batterymonitor.R
import com.example.batterymonitor.databinding.BottomSheetBatteryDetailsBinding
import com.example.batterymonitor.ui.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class BatteryDetailsBottomSheet() : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "BatteryDetailsBottomSheet"
    }

    private val viewModel by activityViewModel<MainViewModel>()

    private var _binding: BottomSheetBatteryDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetBatteryDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setsUpObservers()
    }

    private fun setsUpObservers() {
        viewModel.batteryTechnology.observe(viewLifecycleOwner) {
            binding.textviewBatteryTechnologyBottomSheet.text = it
        }
        viewModel.batteryHealth.observe(viewLifecycleOwner) {
            binding.textviewBatteryHealthBottomSheet.text = getString(it.status())
        }
        viewModel.batteryTemperature.observe(viewLifecycleOwner) {
            binding.textviewBatteryTemperatureBottomSheet.text =
                getString(R.string.battery_temperature_place_holder, it)
        }
        viewModel.batteryVoltage.observe(viewLifecycleOwner) {
            binding.textviewBatteryVoltageBottomSheet.text =
                getString(R.string.battery_voltage_place_holder, it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}