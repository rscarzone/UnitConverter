package com.rosario.unitconverter.ui.converter

import androidx.lifecycle.ViewModel
import com.rosario.unitconverter.domain.Category
import com.rosario.unitconverter.domain.ConversionEngine
import com.rosario.unitconverter.domain.MeasurementUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val engine: ConversionEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConverterUiState())
    val uiState: StateFlow<ConverterUiState> = _uiState.asStateFlow()

    fun onCategoryChange(category: Category) {
        _uiState.update {
            val defaultFrom = category.units.first()
            val defaultTo = category.units.getOrElse(1) { defaultFrom }
            it.copy(category = category, fromUnit = defaultFrom, toUnit = defaultTo)
                .withRecomputedResult()
        }
    }

    fun onFromUnitChange(unit: MeasurementUnit) {
        _uiState.update { it.copy(fromUnit = unit).withRecomputedResult() }
    }

    fun onToUnitChange(unit: MeasurementUnit) {
        _uiState.update { it.copy(toUnit = unit).withRecomputedResult() }
    }

    fun onInputChange(text: String) {
        _uiState.update { it.copy(inputText = text).withRecomputedResult() }
    }

    fun onSwap() {
        _uiState.update {
            it.copy(fromUnit = it.toUnit, toUnit = it.fromUnit).withRecomputedResult()
        }
    }

    private fun ConverterUiState.withRecomputedResult(): ConverterUiState {
        if (inputText.isBlank()) return copy(result = "", errorMessage = null)
        val value = inputText.toDoubleOrNull()
            ?: return copy(result = "", errorMessage = "Please enter a valid number")
        return try {
            val out = engine.convert(value, fromUnit, toUnit)
            copy(result = formatResult(out), errorMessage = null)
        } catch (e: IllegalArgumentException) {
            copy(result = "", errorMessage = e.message ?: "Conversion failed")
        }
    }

    private fun formatResult(value: Double): String {
        // Show up to 6 significant figures, strip trailing zeros
        val formatted = "%.6f".format(value).trimEnd('0').trimEnd('.')
        return if (formatted.isEmpty() || formatted == "-") "0" else formatted
    }
}
