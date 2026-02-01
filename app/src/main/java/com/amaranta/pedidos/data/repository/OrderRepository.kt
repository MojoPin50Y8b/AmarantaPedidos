package com.amaranta.pedidos.data.repository

import com.amaranta.pedidos.data.dao.OrderDao
import com.amaranta.pedidos.data.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val dao: OrderDao) {

    fun getAll(): Flow<List<OrderEntity>> = dao.getAll()
    fun getByDay(start: Long, end: Long) = dao.getByDay(start, end)

    suspend fun insert(order: OrderEntity) = dao.insert(order)
    suspend fun update(order: OrderEntity) = dao.update(order)

    fun observeById(id: Long) = dao.observeById(id)

    suspend fun getById(id: Long) = dao.getById(id)

    suspend fun delete(order: OrderEntity) = dao.delete(order)

    fun observeDeliveredBetween(start: Long, end: Long) = dao.observeDeliveredBetween(start, end)

}