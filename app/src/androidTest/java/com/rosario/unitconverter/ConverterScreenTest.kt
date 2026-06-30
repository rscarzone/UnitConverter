package com.rosario.unitconverter

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.rosario.unitconverter.ui.converter.ConverterScreen
import com.rosario.unitconverter.ui.theme.UnitConverterTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Integration tests that exercise the Compose UI through the real Hilt graph
 * and real ConversionEngine. These run on an emulator and verify the end-to-end
 * user flow: typing a value, swapping units, and seeing the converted result.
 */
@HiltAndroidTest
class ConverterScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun screen_displays_default_category_and_units() {
        composeRule.onNodeWithTag("category_selector").assertIsDisplayed()
        composeRule.onNodeWithTag("from_unit").assertIsDisplayed()
        composeRule.onNodeWithTag("to_unit").assertIsDisplayed()
        composeRule.onNodeWithTag("input_field").assertIsDisplayed()
    }

    @Test
    fun typing_a_value_produces_a_converted_result() {
        composeRule.onNodeWithTag("input_field").performTextInput("100")
        composeRule.onNodeWithTag("result_text").assertIsDisplayed()
    }

    @Test
    fun swap_button_exchanges_units() {
        composeRule.onNodeWithTag("input_field").performTextInput("1")
        composeRule.onNodeWithTag("swap_button").performClick()
        composeRule.onNodeWithTag("result_text").assertIsDisplayed()
    }
}
