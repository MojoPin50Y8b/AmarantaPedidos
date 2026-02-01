package com.amaranta.pedidos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amaranta.pedidos.viewmodel.OrdersViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveredHistoryScreen(
    viewModel: OrdersViewModel,
    onBack: () -> Unit
) {
    val orders by viewModel.deliveredOrders.collectAsStateWithLifecycle()
    val mode by viewModel.deliveredMode.collectAsState()
    val year by viewModel.deliveredYear.collectAsState()
    val month by viewModel.deliveredMonth.collectAsState()
    val day by viewModel.deliveredDay.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Entregados") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("<") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Modo
            RowChips(
                mode = mode,
                onChange = { viewModel.deliveredMode.value = it }
            )

            // Filtros año/mes/día
            FilterRow(
                mode = mode,
                year = year,
                month = month,
                day = day,
                onYear = { viewModel.deliveredYear.value = it },
                onMonth = { viewModel.deliveredMonth.value = it },
                onDay = { viewModel.deliveredDay.value = it }
            )

            // Agrupar por día
            val zoneId = ZoneId.systemDefault()
            val grouped = orders.groupBy { o ->
                val millis = o.deliveredAtMillis ?: o.createdAt
                Instant.ofEpochMilli(millis).atZone(zoneId).toLocalDate()
            }.toSortedMap(compareByDescending { it })

            LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                if (orders.isEmpty()) {
                    item { Text("No hay pedidos entregados en este filtro.") }
                } else {
                    grouped.forEach { (date, list) ->
                        item {
                            Text(
                                text = date.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(list) { o ->
                            ListItem(
                                headlineContent = { Text("${o.arrangementType} - ${o.eventType}") },
                                supportingContent = { Text("Tel: ${o.phone}  |  Estatus: ${o.status}") }
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowChips(
    mode: OrdersViewModel.DeliveredFilterMode,
    onChange: (OrdersViewModel.DeliveredFilterMode) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = mode == OrdersViewModel.DeliveredFilterMode.DAY,
            onClick = { onChange(OrdersViewModel.DeliveredFilterMode.DAY) },
            label = { Text("Día") }
        )
        FilterChip(
            selected = mode == OrdersViewModel.DeliveredFilterMode.MONTH,
            onClick = { onChange(OrdersViewModel.DeliveredFilterMode.MONTH) },
            label = { Text("Mes") }
        )
        FilterChip(
            selected = mode == OrdersViewModel.DeliveredFilterMode.YEAR,
            onClick = { onChange(OrdersViewModel.DeliveredFilterMode.YEAR) },
            label = { Text("Año") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterRow(
    mode: OrdersViewModel.DeliveredFilterMode,
    year: Int,
    month: Int,
    day: Int,
    onYear: (Int) -> Unit,
    onMonth: (Int) -> Unit,
    onDay: (Int) -> Unit
) {
    val years = (2024..(LocalDate.now().year + 1)).toList()
    val months = (1..12).toList()
    val days = (1..31).toList()

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DropdownInt("Año", year, years, onYear)

        if (mode != OrdersViewModel.DeliveredFilterMode.YEAR) {
            DropdownInt("Mes", month, months, onMonth)
        }
        if (mode == OrdersViewModel.DeliveredFilterMode.DAY) {
            DropdownInt("Día", day, days, onDay)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownInt(label: String, value: Int, options: List<Int>, onPick: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = value.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt.toString()) },
                    onClick = { onPick(opt); expanded = false }
                )
            }
        }
    }
}