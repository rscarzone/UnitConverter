package com.rosario.unitconverter.ui.converter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rosario.unitconverter.R
import com.rosario.unitconverter.domain.Category
import com.rosario.unitconverter.domain.MeasurementUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(viewModel: ConverterViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CategorySelector(state.category) { viewModel.onCategoryChange(it) }

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                UnitSelector(
                    label = "From",
                    units = state.category.units,
                    selected = state.fromUnit,
                    modifier = Modifier.weight(1f).testTag("from_unit"),
                    onSelected = viewModel::onFromUnitChange
                )
                IconButton(
                    onClick = viewModel::onSwap,
                    modifier = Modifier.size(48.dp).testTag("swap_button")
                ) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = "Swap units")
                }
                UnitSelector(
                    label = "To",
                    units = state.category.units,
                    selected = state.toUnit,
                    modifier = Modifier.weight(1f).testTag("to_unit"),
                    onSelected = viewModel::onToUnitChange
                )
            }

            OutlinedTextField(
                value = state.inputText,
                onValueChange = viewModel::onInputChange,
                label = { Text("Value to convert") },
                isError = state.errorMessage != null,
                supportingText = state.errorMessage?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth().testTag("input_field")
            )

            Spacer(Modifier.height(4.dp))
            Text("Result", style = MaterialTheme.typography.labelMedium)
            Text(
                text = if (state.result.isEmpty()) "—" else "${state.result} ${state.toUnit.symbol}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.testTag("result_text")
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategorySelector(selected: Category, onSelected: (Category) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selected.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth().testTag("category_selector")
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Category.values().forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.displayName) },
                    onClick = { onSelected(category); expanded = false },
                    modifier = Modifier.testTag("category_option_${category.name}")
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitSelector(
    label: String,
    units: List<MeasurementUnit>,
    selected: MeasurementUnit,
    modifier: Modifier = Modifier,
    onSelected: (MeasurementUnit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }, modifier = modifier) {
        OutlinedTextField(
            value = selected.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            units.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(unit.displayName) },
                    onClick = { onSelected(unit); expanded = false }
                )
            }
        }
    }
}
