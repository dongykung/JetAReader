package com.example.jetareader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetareader.screen.ReaderSplashScreen
import com.example.jetareader.screen.details.BookDetailsScreen
import com.example.jetareader.screen.home.HomeScreen
import com.example.jetareader.screen.home.HomeViewModel
import com.example.jetareader.screen.login.LoginScreen
import com.example.jetareader.screen.search.ReaderSearchScreen
import com.example.jetareader.screen.search.SearchViewModel
import com.example.jetareader.screen.stats.ReaderStatsScreen


@Composable
fun ReaderNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {
        composable(ReaderScreens.SplashScreen.name) {
            ReaderSplashScreen(navController = navController)
        }
        composable(ReaderScreens.ReaderHomeScreen.name) {
            val homeViewModel : HomeViewModel = hiltViewModel()
            HomeScreen(navController = navController,viewModel=homeViewModel)
        }
        composable(ReaderScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(ReaderScreens.ReaderStatsScreen.name) {
            ReaderStatsScreen(navController = navController)
        }
        composable(ReaderScreens.SearchScreen.name) {
            val searchViewModel = hiltViewModel<SearchViewModel>()
            ReaderSearchScreen(navController = navController, searchViewModel) {
                navController.navigate(ReaderScreens.DetailScreen.name+"/${it}")
            }


        }
        val detailName = ReaderScreens.DetailScreen.name
        composable(
            route = "$detailName/{bookId}",
            arguments = listOf(navArgument("bookId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                BookDetailsScreen(navController = navController, bookId = it.toString())
            }
        }
    }
}