package com.amaranta.pedidos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amaranta.pedidos.ui.screens.NewOrderScreen
import com.amaranta.pedidos.ui.screens.OrdersListScreen
import com.amaranta.pedidos.viewmodel.OrdersViewModel

@Composable
fun AppNav(viewModel: OrdersViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LIST,
        modifier = modifier
    ) {
        composable(Routes.LIST) {
            OrdersListScreen(
                viewModel = viewModel,
                onNewOrder = { navController.navigate(Routes.NEW) }
            )
        }

        composable(Routes.NEW) {
            NewOrderScreen(
                onBack = { navController.popBackStack() },
                onSave = { order ->
                    viewModel.add(order)
                    navController.popBackStack()
                }
            )
        }
    }
}