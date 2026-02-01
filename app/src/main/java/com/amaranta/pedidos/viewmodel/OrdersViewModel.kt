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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

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

    private val selectedId = MutableStateFlow<Long?>(null)

    val selectedOrder = selectedId
        .flatMapLatest { id ->
            if (id == null) flowOf(null) else repo.observeById(id)
        }
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000), null)

    fun selectOrder(id: Long) {
        selectedId.value = id
    }

    fun deleteSelected() {
        val order = selectedOrder.value ?: return
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            repo.delete(order)
        }
    }

    fun markDeliveredSelected() {
        val order = selectedOrder.value ?: return
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            repo.update(order.copy(status = "ENTREGADO"))
        }
    }
}