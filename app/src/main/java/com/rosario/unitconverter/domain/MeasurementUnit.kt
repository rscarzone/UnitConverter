package com.rosario.unitconverter.domain

/**
 * A measurement unit, identified by short symbol and a display label. Conversion
 * ratios live in the ConversionEngine to keep this enum a pure value type.
 */
enum class MeasurementUnit(val symbol: String, val displayName: String) {
    // Temperature
    CELSIUS("°C", "Celsius"),
    FAHRENHEIT("°F", "Fahrenheit"),
    KELVIN("K", "Kelvin"),

    // Length
    METER("m", "Meter"),
    KILOMETER("km", "Kilometer"),
    MILE("mi", "Mile"),
    FOOT("ft", "Foot"),
    INCH("in", "Inch"),

    // Weight
    KILOGRAM("kg", "Kilogram"),
    GRAM("g", "Gram"),
    POUND("lb", "Pound"),
    OUNCE("oz", "Ounce");
}
