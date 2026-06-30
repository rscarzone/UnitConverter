package com.rosario.unitconverter.domain

/**
 * A category of measurement and the units it contains. Each unit names its
 * SI-relative or canonical conversion factor used by the ConversionEngine.
 */
enum class Category(val displayName: String, val units: List<MeasurementUnit>) {
    TEMPERATURE(
        displayName = "Temperature",
        units = listOf(MeasurementUnit.CELSIUS, MeasurementUnit.FAHRENHEIT, MeasurementUnit.KELVIN)
    ),
    LENGTH(
        displayName = "Length",
        units = listOf(
            MeasurementUnit.METER, MeasurementUnit.KILOMETER, MeasurementUnit.MILE,
            MeasurementUnit.FOOT, MeasurementUnit.INCH
        )
    ),
    WEIGHT(
        displayName = "Weight",
        units = listOf(
            MeasurementUnit.KILOGRAM, MeasurementUnit.GRAM,
            MeasurementUnit.POUND, MeasurementUnit.OUNCE
        )
    );

    companion object {
        fun categoryFor(unit: MeasurementUnit): Category =
            values().first { it.units.contains(unit) }
    }
}
