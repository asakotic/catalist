package com.example.myapplication.controller

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.main.MainScreen
import com.example.myapplication.main.MainViewModel
import com.example.myapplication.scnd.SecondScreenViewModel
import com.example.myapplication.scnd.SingleView

@Composable
fun Navigation(startDestination: String = Screen.MainScreen.route) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screen.MainScreen.route) {
            val mainViewModel = viewModel<MainViewModel>()
            val state by mainViewModel.state.collectAsState()
            Log.d("anaaa", state.toString())

            MainScreen(
                onClickHead = { id ->
                    val x = Screen.DetailScreen.withArgs(id)
                    navController.navigate(x)
                },
                eventPublisher = { mainViewModel.setEvent(it) },
                state = state
            )
        }
        composable(route = Screen.DetailScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { entry ->
            val idd = entry.arguments?.getString("id") ?: throw IllegalArgumentException("id is required.")

            val secondScreenViewModel = viewModel<SecondScreenViewModel>(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return SecondScreenViewModel(id = idd) as T
                    }
                },
            )
            val state by secondScreenViewModel.state.collectAsState()
            val uriHandler = LocalUriHandler.current
            SingleView(
                onClickBack = {
                    navController.navigateUp()
                },
                onWikiClick = {wiki->
                    uriHandler.openUri(wiki)
                },
                state = state
            )
        }
    }
}



