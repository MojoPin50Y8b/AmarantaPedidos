package com.amaranta.pedidos.ui.navigation

object Routes {
    const val LIST = "list"
    const val NEW = "new"
    const val DETAIL = "detail/{id}"
    fun detail(id: Long) = "detail/$id"
}