package com.amaranta.pedidos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amaranta.pedidos.data.entity.OrderEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewOrderScreen(
    onBack: () -> Unit,
    onSave: (OrderEntity) -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var arrangementType by remember { mutableStateOf("RAMO") }
    var eventType by remember { mutableStateOf("CUMPLE") }
    var notes by remember { mutableStateOf("") }

    val isValid = phone.trim().length >= 7

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Nuevo pedido") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono*") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = arrangementType,
                onValueChange = { arrangementType = it },
                label = { Text("Tipo de arreglo (RAMO/CANASTA/ARREGLO)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = eventType,
                onValueChange = { eventType = it },
                label = { Text("Evento (CUMPLE/BODA/FUNERAL/GRAD...)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notas") },
                modifier = Modifier.fillMaxWidth()
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Cancelar") }

                Button(
                    onClick = {
                        val order = OrderEntity(
                            createdAt = System.currentTimeMillis(),
                            deliveryAt = null,
                            customerName = null,
                            phone = phone.trim(),
                            requiresDelivery = false,
                            deliveryAddress = null,
                            alternateReceiver = null,
                            arrangementType = arrangementType.trim().ifBlank { "RAMO" },
                            eventType = eventType.trim().ifBlank { "CUMPLE" },
                            colors = null,
                            flowers = null,
                            flowerQuantity = null,
                            hasCard = false,
                            cardMessage = null,
                            notes = notes.trim().ifBlank { null },
                            status = "PENDIENTE"
                        )
                        onSave(order)
                    },
                    enabled = isValid,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Guardar pedido") }
            }

            if (!isValid) {
                Text("Ingresa un teléfono válido (mínimo 7 dígitos).")
            }
        }
    }
}