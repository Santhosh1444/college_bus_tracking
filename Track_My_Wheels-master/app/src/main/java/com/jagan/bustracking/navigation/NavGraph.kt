package com.jagan.bustracking.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jagan.bustracking.driver.DriverDashboard
import com.jagan.bustracking.loginScreen.LoginScreen
import com.jagan.bustracking.student.MapScreen
import com.jagan.bustracking.student.StudentDashboard

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {

        composable(Screen.LoginScreen.route){
            LoginScreen(navController)
        }

        composable(Screen.StudentDashboard.route){
            StudentDashboard(navController)
        }

        composable(Screen.DriverDashboard.route){
            DriverDashboard()
        }

        composable(
            Screen.MapScreen.route,
            listOf(navArgument("id"){
                type = NavType.StringType
            })
        ){
            MapScreen(it.arguments?.getString("id").toString())
        }
    }
}