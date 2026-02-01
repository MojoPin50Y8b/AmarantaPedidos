package com.amaranta.pedidos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amaranta.pedidos.viewmodel.OrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    viewModel: OrdersViewModel,
    onBack: () -> Unit
) {
    val order by viewModel.selectedOrder.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Detalle del pedido") }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (order == null) {
                Text("Cargando pedido...")
                Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Volver") }
                return@Column
            }

            val o = order!!

            Text("ID: ${o.id}")
            Text("Cliente: ${o.customerName ?: "—"}")
            Text("Tel: ${o.phone}")
            Text("Tipo: ${o.arrangementType}")
            Text("Evento: ${o.eventType}")
            Text("Entrega: ${if (o.requiresDelivery) "Sí" else "No"}")
            if (o.requiresDelivery) {
                Text("Dirección: ${o.deliveryAddress ?: "—"}")
                Text("Alterno: ${o.alternateReceiver ?: "—"}")
            }
            Text("Colores: ${o.colors ?: "—"}")
            Text("Flores: ${o.flowers ?: "—"}")
            Text("Cantidad: ${o.flowerQuantity?.toString() ?: "—"}")
            Text("Tarjeta: ${if (o.hasCard) "Sí" else "No"}")
            if (o.hasCard) Text("Mensaje: ${o.cardMessage ?: "—"}")
            Text("Notas: ${o.notes ?: "—"}")
            Text("Estatus: ${o.status}")

            Button(
                onClick = { viewModel.markDeliveredSelected(); onBack() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Marcar como ENTREGADO") }

            Button(
                onClick = { viewModel.deleteSelected(); onBack() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Eliminar pedido") }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Volver") }
        }
    }
}