package com.jagan.bustracking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.jagan.bustracking.backgroundlocation.LocationService
import com.jagan.bustracking.navigation.NavGraph
import com.jagan.bustracking.ui.theme.BusTrackingTheme
import kotlinx.coroutines.CoroutineScope

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            BusTrackingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    appContext = LocalContext.current
                    launchScope = rememberCoroutineScope()
                    NavGraph(navController = rememberNavController())
                }
            }
        }
    }


    companion object {
        lateinit var appContext: Context
        lateinit var launchScope :CoroutineScope
        fun startLocationService(context: Context) {
            val intent = Intent(context, LocationService::class.java)
            intent.action = LocationService.ACTION_START
            context.startService(intent)
        }

        fun stopLocationService(context: Context) {
            val intent = Intent(context, LocationService::class.java)
            intent.action = LocationService.ACTION_STOP
            context.stopService(intent)
        }
    }

}
