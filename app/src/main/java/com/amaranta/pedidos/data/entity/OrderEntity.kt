package com.amaranta.pedidos.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val createdAt: Long,
    val deliveryAt: Long?,

    val customerName: String?,
    val phone: String,

    val requiresDelivery: Boolean,
    val deliveryAddress: String?,
    val alternateReceiver: String?,

    val arrangementType: String, // RAMO, CANASTA, ARREGLO
    val eventType: String,       // CUMPLE, BODA, FUNERAL, ETC

    val colors: String?,
    val flowers: String?,
    val flowerQuantity: Int?,

    val hasCard: Boolean,
    val cardMessage: String?,

    val notes: String?,
    val status: String = "PENDIENTE",

    val deliveredAtMillis: Long? = null
)