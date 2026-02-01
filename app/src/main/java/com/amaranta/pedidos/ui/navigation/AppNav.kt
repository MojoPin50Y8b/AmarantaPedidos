package com.amaranta.pedidos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amaranta.pedidos.ui.screens.DeliveredHistoryScreen
import com.amaranta.pedidos.ui.screens.NewOrderScreen
import com.amaranta.pedidos.ui.screens.OrderDetailScreen
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
                onNewOrder = { navController.navigate(Routes.NEW) },
                onOpenDetail = { id ->
                    viewModel.selectOrder(id)              // ✅ selección antes
                    navController.navigate(Routes.detail(id))
                },
                onOpenDelivered = { navController.navigate(Routes.DELIVERED) }
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

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            viewModel.selectOrder(id) // ✅ ok dejarlo también

            OrderDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.DELIVERED) {
            DeliveredHistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenDetail = { id ->
                    viewModel.selectOrder(id)              // ✅ selección antes
                    navController.navigate(Routes.detail(id))
                }
            )
        }
    }
}