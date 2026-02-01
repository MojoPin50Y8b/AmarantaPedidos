package com.amaranta.pedidos.data.repository

import com.amaranta.pedidos.data.dao.OrderDao
import com.amaranta.pedidos.data.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val dao: OrderDao) {

    // --- Listas ---
    fun getAll(): Flow<List<OrderEntity>> = dao.getAll()

    // Pantalla principal: SOLO no entregados
    fun observePending(): Flow<List<OrderEntity>> = dao.observePending()

    // Entregados por rango (día/mes/año)
    fun observeDeliveredBetween(start: Long, end: Long): Flow<List<OrderEntity>> =
        dao.observeDeliveredBetween(start, end)

    // (Opcional) por día (creados ese día)
    fun getByDay(start: Long, end: Long): Flow<List<OrderEntity>> = dao.getByDay(start, end)

    // --- Detalle ---
    fun observeById(id: Long) = dao.observeById(id)
    suspend fun getById(id: Long) = dao.getById(id)

    // --- CRUD ---
    suspend fun insert(order: OrderEntity) = dao.insert(order)

    // OJO: en tu DAO update regresa Int, aquí lo exponemos igual por si quieres validar filas afectadas
    suspend fun update(order: OrderEntity): Int = dao.update(order)

    suspend fun delete(order: OrderEntity) = dao.delete(order)
}