package com.rosario.unitconverter.ui.converter

import com.google.common.truth.Truth.assertThat
import com.rosario.unitconverter.domain.Category
import com.rosario.unitconverter.domain.ConversionEngine
import com.rosario.unitconverter.domain.MeasurementUnit
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ConverterViewModelTest {

    private lateinit var engine: ConversionEngine
    private lateinit var viewModel: ConverterViewModel

    @Before
    fun setUp() {
        engine = mockk()
        every { engine.convert(any(), any(), any()) } answers {
            val v = firstArg<Double>()
            val from = secondArg<MeasurementUnit>()
            val to = thirdArg<MeasurementUnit>()
            // Simple stub: if same units return value, else multiply by 2 for predictability
            if (from == to) v else v * 2.0
        }
        viewModel = ConverterViewModel(engine)
    }

    @Test
    fun `initial state has default Length category and Meter to Foot units`() {
        val state = viewModel.uiState.value
        assertThat(state.category).isEqualTo(Category.LENGTH)
        assertThat(state.fromUnit).isEqualTo(MeasurementUnit.METER)
        assertThat(state.toUnit).isEqualTo(MeasurementUnit.FOOT)
        assertThat(state.result).isEmpty()
    }

    @Test
    fun `changing category resets units to category defaults`() {
        viewModel.onCategoryChange(Category.TEMPERATURE)
        val state = viewModel.uiState.value
        assertThat(state.category).isEqualTo(Category.TEMPERATURE)
        assertThat(state.fromUnit).isEqualTo(MeasurementUnit.CELSIUS)
        assertThat(state.toUnit).isEqualTo(MeasurementUnit.FAHRENHEIT)
    }

    @Test
    fun `entering numeric input triggers conversion and updates result`() {
        viewModel.onInputChange("5.0")
        verify { engine.convert(5.0, MeasurementUnit.METER, MeasurementUnit.FOOT) }
        assertThat(viewModel.uiState.value.result).isEqualTo("10")
        assertThat(viewModel.uiState.value.errorMessage).isNull()
    }

    @Test
    fun `entering blank input clears the result with no error`() {
        viewModel.onInputChange("5.0")
        viewModel.onInputChange("")
        val state = viewModel.uiState.value
        assertThat(state.result).isEmpty()
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `entering non-numeric input sets error message and clears result`() {
        viewModel.onInputChange("abc")
        val state = viewModel.uiState.value
        assertThat(state.result).isEmpty()
        assertThat(state.errorMessage).isNotNull()
    }

    @Test
    fun `swap exchanges from and to units and recomputes`() {
        viewModel.onInputChange("3.0")
        val before = viewModel.uiState.value
        viewModel.onSwap()
        val after = viewModel.uiState.value
        assertThat(after.fromUnit).isEqualTo(before.toUnit)
        assertThat(after.toUnit).isEqualTo(before.fromUnit)
    }
}
