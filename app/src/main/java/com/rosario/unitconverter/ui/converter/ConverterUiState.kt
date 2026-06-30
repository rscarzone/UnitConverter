package com.rosario.unitconverter.ui.converter

import com.rosario.unitconverter.domain.Category
import com.rosario.unitconverter.domain.MeasurementUnit

data class ConverterUiState(
    val category: Category = Category.LENGTH,
    val fromUnit: MeasurementUnit = MeasurementUnit.METER,
    val toUnit: MeasurementUnit = MeasurementUnit.FOOT,
    val inputText: String = "",
    val result: String = "",
    val errorMessage: String? = null
)
