package com.amaranta.pedidos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amaranta.pedidos.data.entity.OrderEntity
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

private val ARRANGEMENTS = listOf("RAMO", "CANASTA", "ARREGLO", "OTRO")
private val EVENTS =
    listOf("CUMPLEAÑOS", "FIESTA", "BODA", "ZEPelio", "GRADUACIÓN", "ANIVERSARIO", "OTRO")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewOrderScreen(
    onBack: () -> Unit,
    onSave: (OrderEntity) -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // Dropdowns
    var arrangementType by remember { mutableStateOf("RAMO") }
    var eventType by remember { mutableStateOf("CUMPLEAÑOS") }
    var arrangementExpanded by remember { mutableStateOf(false) }
    var eventExpanded by remember { mutableStateOf(false) }

    // Detalles del arreglo
    var colors by remember { mutableStateOf("") }
    var flowers by remember { mutableStateOf("") }
    var flowerQty by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Entrega
    var requiresDelivery by remember { mutableStateOf(false) }
    var deliveryAddress by remember { mutableStateOf("") }
    var deliveryTimeText by remember { mutableStateOf("") } // por ahora texto (ej: 18:30)
    var alternateReceiver by remember { mutableStateOf("") }

    // Tarjeta
    var hasCard by remember { mutableStateOf(false) }
    var cardMessage by remember { mutableStateOf("") }

    // Validación mínima
    val phoneOk = phone.trim().length >= 7
    val deliveryOk = !requiresDelivery || deliveryAddress.isNotBlank()
    val canSave = phoneOk && deliveryOk

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuevo pedido") }) }
    ) { padding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Nombre del cliente (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono*") },
                modifier = Modifier.fillMaxWidth()
            )

            // Tipo de arreglo (Dropdown)
            ExposedDropdownMenuBox(
                expanded = arrangementExpanded,
                onExpandedChange = { arrangementExpanded = !arrangementExpanded }
            ) {
                OutlinedTextField(
                    value = arrangementType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de arreglo*") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = arrangementExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = arrangementExpanded,
                    onDismissRequest = { arrangementExpanded = false }
                ) {
                    ARRANGEMENTS.forEach { opt ->
                        DropdownMenuItem(
                            text = { Text(opt) },
                            onClick = {
                                arrangementType = opt
                                arrangementExpanded = false
                            }
                        )
                    }
                }
            }

            // Tipo de evento (Dropdown)
            ExposedDropdownMenuBox(
                expanded = eventExpanded,
                onExpandedChange = { eventExpanded = !eventExpanded }
            ) {
                OutlinedTextField(
                    value = eventType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Evento*") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = eventExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = eventExpanded,
                    onDismissRequest = { eventExpanded = false }
                ) {
                    EVENTS.forEach { opt ->
                        DropdownMenuItem(
                            text = { Text(opt) },
                            onClick = {
                                eventType = opt
                                eventExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = colors,
                onValueChange = { colors = it },
                label = { Text("Color(es)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = flowers,
                onValueChange = { flowers = it },
                label = { Text("Flor(es)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = flowerQty,
                onValueChange = { flowerQty = it.filter { ch -> ch.isDigit() } },
                label = { Text("Cantidad de flores") },
                modifier = Modifier.fillMaxWidth()
            )

            // Entrega
            RowSwitch(
                label = "¿Requiere entrega?",
                checked = requiresDelivery,
                onCheckedChange = { requiresDelivery = it }
            )

            if (requiresDelivery) {
                OutlinedTextField(
                    value = deliveryAddress,
                    onValueChange = { deliveryAddress = it },
                    label = { Text("Dirección de entrega*") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = deliveryTimeText,
                    onValueChange = { deliveryTimeText = it },
                    label = { Text("Hora de entrega (ej: 18:30)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = alternateReceiver,
                    onValueChange = { alternateReceiver = it },
                    label = { Text("Persona alternativa (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Tarjeta
            RowSwitch(
                label = "¿Lleva tarjeta?",
                checked = hasCard,
                onCheckedChange = { hasCard = it }
            )

            if (hasCard) {
                OutlinedTextField(
                    value = cardMessage,
                    onValueChange = { cardMessage = it },
                    label = { Text("Mensaje de la tarjeta") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notas") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Cancelar") }

            Button(
                onClick = {
                    val qty = flowerQty.toIntOrNull()

                    val order = OrderEntity(
                        createdAt = System.currentTimeMillis(),
                        deliveryAt = null, // lo convertiremos a timestamp más adelante
                        customerName = customerName.trim().ifBlank { null },
                        phone = phone.trim(),
                        requiresDelivery = requiresDelivery,
                        deliveryAddress = deliveryAddress.trim().ifBlank { null },
                        alternateReceiver = alternateReceiver.trim().ifBlank { null },
                        arrangementType = arrangementType,
                        eventType = eventType,
                        colors = colors.trim().ifBlank { null },
                        flowers = flowers.trim().ifBlank { null },
                        flowerQuantity = qty,
                        hasCard = hasCard,
                        cardMessage = cardMessage.trim().ifBlank { null },
                        notes = notes.trim().ifBlank { null },
                        status = "PENDIENTE"
                    )

                    onSave(order)
                },
                enabled = canSave,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar pedido") }

            if (!phoneOk) Text("Teléfono inválido (mínimo 7 dígitos).")
            if (!deliveryOk) Text("Si requiere entrega, la dirección es obligatoria.")
        }
    }
}

@Composable
private fun RowSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}