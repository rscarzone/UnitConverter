package com.rosario.unitconverter.domain

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Pure conversion logic. No Android dependencies, so every method is unit
 * testable on the JVM without an emulator. Length and weight conversions go
 * through a canonical unit (meters and kilograms respectively); temperature
 * is handled with explicit formulas because it is non-linear in absolute terms.
 */
@Singleton
class ConversionEngine @Inject constructor() {

    fun convert(value: Double, from: MeasurementUnit, to: MeasurementUnit): Double {
        if (from == to) return value
        require(Category.categoryFor(from) == Category.categoryFor(to)) {
            "Cannot convert between different categories: ${'$'}from and ${'$'}to"
        }
        return when (Category.categoryFor(from)) {
            Category.TEMPERATURE -> convertTemperature(value, from, to)
            Category.LENGTH -> value * lengthToMeter(from) / lengthToMeter(to)
            Category.WEIGHT -> value * weightToKilogram(from) / weightToKilogram(to)
        }
    }

    private fun convertTemperature(value: Double, from: MeasurementUnit, to: MeasurementUnit): Double {
        val celsius = when (from) {
            MeasurementUnit.CELSIUS -> value
            MeasurementUnit.FAHRENHEIT -> (value - 32.0) * 5.0 / 9.0
            MeasurementUnit.KELVIN -> value - 273.15
            else -> error("Not a temperature unit: ${'$'}from")
        }
        return when (to) {
            MeasurementUnit.CELSIUS -> celsius
            MeasurementUnit.FAHRENHEIT -> celsius * 9.0 / 5.0 + 32.0
            MeasurementUnit.KELVIN -> celsius + 273.15
            else -> error("Not a temperature unit: ${'$'}to")
        }
    }

    private fun lengthToMeter(unit: MeasurementUnit): Double = when (unit) {
        MeasurementUnit.METER -> 1.0
        MeasurementUnit.KILOMETER -> 1000.0
        MeasurementUnit.MILE -> 1609.344
        MeasurementUnit.FOOT -> 0.3048
        MeasurementUnit.INCH -> 0.0254
        else -> error("Not a length unit: ${'$'}unit")
    }

    private fun weightToKilogram(unit: MeasurementUnit): Double = when (unit) {
        MeasurementUnit.KILOGRAM -> 1.0
        MeasurementUnit.GRAM -> 0.001
        MeasurementUnit.POUND -> 0.45359237
        MeasurementUnit.OUNCE -> 0.028349523125
        else -> error("Not a weight unit: ${'$'}unit")
    }
}
