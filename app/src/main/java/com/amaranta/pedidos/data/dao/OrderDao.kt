package com.amaranta.pedidos.data.dao

import androidx.room.*
import com.amaranta.pedidos.data.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    // --------
    // CRUD
    // --------
    @Insert
    suspend fun insert(order: OrderEntity)

    @Update
    suspend fun update(order: OrderEntity): Int

    @Delete
    suspend fun delete(order: OrderEntity)

    // --------
    // LISTAS
    // --------

    // Todo (si lo necesitas para debug/admin)
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAll(): Flow<List<OrderEntity>>

    // ✅ SOLO pendientes / no entregados (pantalla principal)
    @Query("""
        SELECT * FROM orders
        WHERE status != 'ENTREGADO'
        ORDER BY createdAt DESC
    """)
    fun observePending(): Flow<List<OrderEntity>>

    // ✅ ENTREGADOS por rango (end EXCLUSIVO)
    // COALESCE evita que se "pierdan" si deliveredAtMillis quedara null
    @Query("""
        SELECT * FROM orders
        WHERE status = 'ENTREGADO'
          AND COALESCE(deliveredAtMillis, createdAt) >= :start
          AND COALESCE(deliveredAtMillis, createdAt) < :end
        ORDER BY COALESCE(deliveredAtMillis, createdAt) DESC
    """)
    fun observeDeliveredBetween(start: Long, end: Long): Flow<List<OrderEntity>>

    // (Opcional) Pedidos creados por día
    @Query("""
        SELECT * FROM orders
        WHERE createdAt >= :start AND createdAt < :end
        ORDER BY createdAt DESC
    """)
    fun getByDay(start: Long, end: Long): Flow<List<OrderEntity>>

    // --------
    // DETALLE
    // --------
    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): OrderEntity?

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<OrderEntity?>
}