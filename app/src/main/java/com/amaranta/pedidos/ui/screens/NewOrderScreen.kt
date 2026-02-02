package com.amaranta.pedidos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.amaranta.pedidos.data.entity.OrderEntity

private val ARRANGEMENTS = listOf("RAMO", "CANASTA", "ARREGLO", "OTRO")
private val EVENTS = listOf("CUMPLEAÑOS", "FIESTA", "BODA", "ZEPelio", "GRADUACIÓN", "ANIVERSARIO", "OTRO")

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
    var deliveryTimeText by remember { mutableStateOf("") } // HH:mm
    var alternateReceiver by remember { mutableStateOf("") }

    // Tarjeta
    var hasCard by remember { mutableStateOf(false) }
    var cardMessage by remember { mutableStateOf("") }

    // Validación (mostrar errores solo cuando el usuario ya tocó algo)
    var triedSave by remember { mutableStateOf(false) }

    val phoneOk = phone.trim().length >= 7
    val deliveryOk = !requiresDelivery || deliveryAddress.isNotBlank()
    val canSave = phoneOk && deliveryOk

    val showPhoneError = triedSave && !phoneOk
    val showDeliveryError = triedSave && !deliveryOk

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo pedido") }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Nombre del cliente (opcional)") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Teléfono*") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = showPhoneError,
                    supportingText = {
                        if (showPhoneError) {
                            Text(
                                "Teléfono inválido (mínimo 7 dígitos).",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Tipo de arreglo (Dropdown)
            item {
                ExposedDropdownMenuBox(
                    expanded = arrangementExpanded,
                    onExpandedChange = { arrangementExpanded = !arrangementExpanded }
                ) {
                    OutlinedTextField(
                        value = arrangementType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de arreglo*") },
                        leadingIcon = { Icon(Icons.Default.LocalFlorist, contentDescription = null) },
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
            }

            // Tipo de evento (Dropdown)
            item {
                ExposedDropdownMenuBox(
                    expanded = eventExpanded,
                    onExpandedChange = { eventExpanded = !eventExpanded }
                ) {
                    OutlinedTextField(
                        value = eventType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Evento*") },
                        leadingIcon = { Icon(Icons.Default.Event, contentDescription = null) },
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
            }

            item {
                OutlinedTextField(
                    value = colors,
                    onValueChange = { colors = it },
                    label = { Text("Color(es)") },
                    leadingIcon = { Icon(Icons.Default.ShoppingBag, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = flowers,
                    onValueChange = { flowers = it },
                    label = { Text("Flor(es)") },
                    leadingIcon = { Icon(Icons.Default.LocalFlorist, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = flowerQty,
                    onValueChange = { flowerQty = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Cantidad de flores") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Divider()
            }

            // Entrega
            item {
                SectionHeader(title = "Entrega")
            }

            item {
                RowSwitch(
                    label = "¿Requiere entrega?",
                    checked = requiresDelivery,
                    onCheckedChange = { requiresDelivery = it }
                )
            }

            if (requiresDelivery) {
                item {
                    OutlinedTextField(
                        value = deliveryAddress,
                        onValueChange = { deliveryAddress = it },
                        label = { Text("Dirección de entrega*") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                        isError = showDeliveryError,
                        supportingText = {
                            if (showDeliveryError) {
                                Text(
                                    "Si requiere entrega, la dirección es obligatoria.",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    TimePickerField(
                        label = "Hora de entrega",
                        value = deliveryTimeText,
                        onValueChange = { deliveryTimeText = it }
                    )
                }

                item {
                    OutlinedTextField(
                        value = alternateReceiver,
                        onValueChange = { alternateReceiver = it },
                        label = { Text("Persona alternativa (opcional)") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                Divider()
            }

            // Tarjeta
            item {
                SectionHeader(title = "Tarjeta")
            }

            item {
                RowSwitch(
                    label = "¿Lleva tarjeta?",
                    checked = hasCard,
                    onCheckedChange = { hasCard = it }
                )
            }

            if (hasCard) {
                item {
                    OutlinedTextField(
                        value = cardMessage,
                        onValueChange = { cardMessage = it },
                        label = { Text("Mensaje de la tarjeta") },
                        leadingIcon = { Icon(Icons.Default.NoteAlt, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notas") },
                    leadingIcon = { Icon(Icons.Default.NoteAlt, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Botones
            item {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Cancelar")
                }
            }

            item {
                Button(
                    onClick = {
                        triedSave = true
                        if (!canSave) return@Button

                        val qty = flowerQty.toIntOrNull()

                        val order = OrderEntity(
                            createdAt = System.currentTimeMillis(),
                            deliveryAt = null, // puedes convertir deliveryTimeText después si quieres
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
                            status = "PENDIENTE",
                            deliveredAtMillis = null
                        )

                        onSave(order)
                    },
                    enabled = canSave || !triedSave, // deja intentar para mostrar errores
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Text("  Guardar pedido")
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
private fun RowSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    var open by remember { mutableStateOf(false) }
    val state = rememberTimePickerState(is24Hour = true)

    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null) },
        trailingIcon = {
            IconButton(onClick = { open = true }) {
                Icon(Icons.Default.AccessTime, contentDescription = "Elegir hora")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    if (open) {
        AlertDialog(
            onDismissRequest = { open = false },
            confirmButton = {
                TextButton(onClick = {
                    val hh = state.hour.toString().padStart(2, '0')
                    val mm = state.minute.toString().padStart(2, '0')
                    onValueChange("$hh:$mm")
                    open = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { open = false }) { Text("Cancelar") }
            },
            title = { Text("Selecciona hora") },
            text = { TimePicker(state = state) }
        )
    }
}