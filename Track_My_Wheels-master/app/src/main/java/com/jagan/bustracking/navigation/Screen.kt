package com.jagan.bustracking.navigation

sealed class Screen(val route : String){
    object LoginScreen:Screen("login_screen")
    object DriverDashboard:Screen("driver_dashboard")
    object StudentDashboard:Screen("student_dashboard")
    object MapScreen:Screen("map_screen/{id}")
}

sealed class DriverScreen(val route : String){
    object TrackScreen:Screen("track_screen")
    object UpdateScreen:Screen("update_screen")
}
