package com.amaranta.pedidos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amaranta.pedidos.data.db.DatabaseProvider
import com.amaranta.pedidos.data.entity.OrderEntity
import com.amaranta.pedidos.data.repository.OrderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrdersViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = OrderRepository(
        DatabaseProvider.get(app).orderDao()
    )

    val orders = repo.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun add(order: OrderEntity) {
        viewModelScope.launch {
            repo.insert(order)
        }
    }

    fun insertDummyOrder() {
        add(
            com.amaranta.pedidos.data.entity.OrderEntity(
                createdAt = System.currentTimeMillis(),
                deliveryAt = null,
                customerName = "Cliente prueba",
                phone = "5551234567",
                requiresDelivery = false,
                deliveryAddress = null,
                alternateReceiver = null,
                arrangementType = "RAMO",
                eventType = "CUMPLE",
                colors = "Amarillo",
                flowers = "Rosas",
                flowerQuantity = 12,
                hasCard = true,
                cardMessage = "¡Felicidades!",
                notes = "Pedido de prueba",
                status = "PENDIENTE"
            )
        )
    }
}