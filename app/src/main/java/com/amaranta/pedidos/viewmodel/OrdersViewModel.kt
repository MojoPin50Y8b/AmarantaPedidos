package com.amaranta.pedidos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amaranta.pedidos.data.db.DatabaseProvider
import com.amaranta.pedidos.data.entity.OrderEntity
import com.amaranta.pedidos.data.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

class OrdersViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = OrderRepository(
        DatabaseProvider.get(app).orderDao()
    )

    // ✅ LISTA PRINCIPAL: SOLO PENDIENTES
    val orders = repo.observePending()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun add(order: OrderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(order)
        }
    }

    fun insertDummyOrder() {
        add(
            OrderEntity(
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
                status = "PENDIENTE",
                deliveredAtMillis = null
            )
        )
    }

    // ----------------------------
    // DETALLE SELECCIONADO
    // ----------------------------
    private val selectedId = MutableStateFlow<Long?>(null)

    val selectedOrder = selectedId
        .flatMapLatest { id ->
            if (id == null) flowOf(null) else repo.observeById(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun selectOrder(id: Long) {
        selectedId.value = id
    }

    fun deleteSelected() {
        val order = selectedOrder.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repo.delete(order)
        }
    }

    fun markDeliveredSelected() {
        val order = selectedOrder.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repo.update(
                order.copy(
                    status = "ENTREGADO",
                    deliveredAtMillis = System.currentTimeMillis()
                )
            )
        }
    }

    // ----------------------------
    // ENTREGADOS (HISTORIAL)
    // ----------------------------
    enum class DeliveredFilterMode { DAY, MONTH, YEAR }

    private val zoneId = ZoneId.systemDefault()

    val deliveredMode = MutableStateFlow(DeliveredFilterMode.DAY)
    val deliveredYear = MutableStateFlow(LocalDate.now().year)
    val deliveredMonth = MutableStateFlow(LocalDate.now().monthValue) // 1..12
    val deliveredDay = MutableStateFlow(LocalDate.now().dayOfMonth)

    // ✅ IMPORTANTE: END EXCLUSIVO (sin -1)
    private fun dayRangeMillis(year: Int, month: Int, day: Int): Pair<Long, Long> {
        val start = LocalDate.of(year, month, day)
            .atStartOfDay(zoneId).toInstant().toEpochMilli()
        val end = LocalDate.of(year, month, day)
            .plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli()
        return start to end
    }

    private fun monthRangeMillis(year: Int, month: Int): Pair<Long, Long> {
        val ym = YearMonth.of(year, month)
        val start = ym.atDay(1).atStartOfDay(zoneId).toInstant().toEpochMilli()
        val end = ym.atEndOfMonth().plusDays(1)
            .atStartOfDay(zoneId).toInstant().toEpochMilli()
        return start to end
    }

    private fun yearRangeMillis(year: Int): Pair<Long, Long> {
        val start = LocalDate.of(year, 1, 1)
            .atStartOfDay(zoneId).toInstant().toEpochMilli()
        val end = LocalDate.of(year + 1, 1, 1)
            .atStartOfDay(zoneId).toInstant().toEpochMilli()
        return start to end
    }

    // ✅ ENTREGADOS: SOLO status='ENTREGADO' + FILTRO POR RANGO
    val deliveredOrders = combine(
        deliveredMode,
        deliveredYear,
        deliveredMonth,
        deliveredDay
    ) { mode, year, month, day ->
        Quad(mode, year, month, day)
    }.flatMapLatest { (mode, year, month, day) ->
        when (mode) {
            DeliveredFilterMode.DAY -> {
                val (s, e) = dayRangeMillis(year, month, day)
                repo.observeDeliveredBetween(s, e)
            }
            DeliveredFilterMode.MONTH -> {
                val (s, e) = monthRangeMillis(year, month)
                repo.observeDeliveredBetween(s, e)
            }
            DeliveredFilterMode.YEAR -> {
                val (s, e) = yearRangeMillis(year)
                repo.observeDeliveredBetween(s, e)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Helper simple (porque Kotlin no trae Quad)
    data class Quad<A, B, C, D>(
        val first: A,
        val second: B,
        val third: C,
        val fourth: D
    )
}