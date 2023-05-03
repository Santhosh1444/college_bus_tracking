package com.jagan.bustracking.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jagan.bustracking.driver.TrackScreen
import com.jagan.bustracking.driver.UpdateScreen



@Composable
fun DriverNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = DriverScreen.UpdateScreen.route
    ) {

        composable(DriverScreen.UpdateScreen.route){
            UpdateScreen()
        }

        composable(DriverScreen.TrackScreen.route){
            TrackScreen()
        }
    }
}
