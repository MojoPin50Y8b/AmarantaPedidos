package com.amaranta.pedidos.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amaranta.pedidos.data.dao.OrderDao
import com.amaranta.pedidos.data.entity.OrderEntity

@Database(
    entities = [OrderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
}