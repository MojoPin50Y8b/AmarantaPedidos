package com.amaranta.pedidos.data.dao

import androidx.room.*
import com.amaranta.pedidos.data.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Insert
    suspend fun insert(order: OrderEntity)

    @Update
    suspend fun update(order: OrderEntity)

    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAll(): Flow<List<OrderEntity>>

    @Query("""
        SELECT * FROM orders 
        WHERE createdAt BETWEEN :start AND :end
        ORDER BY createdAt DESC
    """)
    fun getByDay(start: Long, end: Long): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): OrderEntity?

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    fun observeById(id: Long): kotlinx.coroutines.flow.Flow<OrderEntity?>

    @Delete
    suspend fun delete(order: OrderEntity)
}