package com.amaranta.pedidos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.amaranta.pedidos.ui.navigation.AppNav
import com.amaranta.pedidos.ui.theme.AmarantaPedidosTheme
import com.amaranta.pedidos.viewmodel.OrdersViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: OrdersViewModel =
            ViewModelProvider(this)[OrdersViewModel::class.java]

        setContent {
            AmarantaPedidosTheme {
                AppNav(viewModel)
            }
        }
    }
}