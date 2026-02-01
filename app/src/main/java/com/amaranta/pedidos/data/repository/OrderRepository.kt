package com.amaranta.pedidos.data.repository

import com.amaranta.pedidos.data.dao.OrderDao
import com.amaranta.pedidos.data.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val dao: OrderDao) {

    fun getAll(): Flow<List<OrderEntity>> = dao.getAll()
    fun getByDay(start: Long, end: Long) = dao.getByDay(start, end)

    suspend fun insert(order: OrderEntity) = dao.insert(order)
    suspend fun update(order: OrderEntity) = dao.update(order)
}