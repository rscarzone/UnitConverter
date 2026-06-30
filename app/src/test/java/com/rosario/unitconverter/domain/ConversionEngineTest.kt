package com.rosario.unitconverter.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.math.abs

class ConversionEngineTest {

    private lateinit var engine: ConversionEngine

    @Before
    fun setUp() {
        engine = ConversionEngine()
    }

    // ---- Temperature ----

    @Test
    fun `100 Celsius equals 212 Fahrenheit`() {
        val result = engine.convert(100.0, MeasurementUnit.CELSIUS, MeasurementUnit.FAHRENHEIT)
        assertThat(result).isWithin(EPSILON).of(212.0)
    }

    @Test
    fun `0 Celsius equals 32 Fahrenheit`() {
        val result = engine.convert(0.0, MeasurementUnit.CELSIUS, MeasurementUnit.FAHRENHEIT)
        assertThat(result).isWithin(EPSILON).of(32.0)
    }

    @Test
    fun `100 Celsius equals 373_15 Kelvin`() {
        val result = engine.convert(100.0, MeasurementUnit.CELSIUS, MeasurementUnit.KELVIN)
        assertThat(result).isWithin(EPSILON).of(373.15)
    }

    @Test
    fun `Fahrenheit to Celsius round trip preserves value`() {
        val original = 75.5
        val toC = engine.convert(original, MeasurementUnit.FAHRENHEIT, MeasurementUnit.CELSIUS)
        val backToF = engine.convert(toC, MeasurementUnit.CELSIUS, MeasurementUnit.FAHRENHEIT)
        assertThat(backToF).isWithin(EPSILON).of(original)
    }

    @Test
    fun `Negative Celsius converts to Fahrenheit correctly`() {
        val result = engine.convert(-40.0, MeasurementUnit.CELSIUS, MeasurementUnit.FAHRENHEIT)
        assertThat(result).isWithin(EPSILON).of(-40.0)
    }

    // ---- Length ----

    @Test
    fun `1 meter equals 3_28084 feet`() {
        val result = engine.convert(1.0, MeasurementUnit.METER, MeasurementUnit.FOOT)
        assertThat(result).isWithin(EPSILON).of(3.28084)
    }

    @Test
    fun `1 mile equals 1609_344 meters`() {
        val result = engine.convert(1.0, MeasurementUnit.MILE, MeasurementUnit.METER)
        assertThat(result).isWithin(EPSILON).of(1609.344)
    }

    @Test
    fun `12 inches equals 1 foot`() {
        val result = engine.convert(12.0, MeasurementUnit.INCH, MeasurementUnit.FOOT)
        assertThat(result).isWithin(EPSILON).of(1.0)
    }

    @Test
    fun `Length round trip across multiple units`() {
        val original = 5.0
        val km = engine.convert(original, MeasurementUnit.MILE, MeasurementUnit.KILOMETER)
        val backToMi = engine.convert(km, MeasurementUnit.KILOMETER, MeasurementUnit.MILE)
        assertThat(backToMi).isWithin(EPSILON).of(original)
    }

    // ---- Weight ----

    @Test
    fun `1 kilogram equals 1000 grams`() {
        val result = engine.convert(1.0, MeasurementUnit.KILOGRAM, MeasurementUnit.GRAM)
        assertThat(result).isWithin(EPSILON).of(1000.0)
    }

    @Test
    fun `1 pound equals 16 ounces`() {
        val result = engine.convert(1.0, MeasurementUnit.POUND, MeasurementUnit.OUNCE)
        assertThat(result).isWithin(EPSILON).of(16.0)
    }

    @Test
    fun `1 kilogram equals approximately 2_2046 pounds`() {
        val result = engine.convert(1.0, MeasurementUnit.KILOGRAM, MeasurementUnit.POUND)
        assertThat(abs(result - 2.2046)).isLessThan(0.001)
    }

    // ---- Identity and edge cases ----

    @Test
    fun `Same unit conversion returns input unchanged`() {
        val result = engine.convert(42.5, MeasurementUnit.METER, MeasurementUnit.METER)
        assertThat(result).isEqualTo(42.5)
    }

    @Test
    fun `Zero value converts to zero`() {
        val result = engine.convert(0.0, MeasurementUnit.METER, MeasurementUnit.FOOT)
        assertThat(result).isEqualTo(0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Cross-category conversion throws`() {
        engine.convert(100.0, MeasurementUnit.METER, MeasurementUnit.CELSIUS)
    }

    companion object {
        private const val EPSILON = 0.001
    }
}
