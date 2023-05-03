package com.jagan.bustracking.driver

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jagan.bustracking.R
import com.jagan.bustracking.navigation.DriverNavGraph
import com.jagan.bustracking.navigation.DriverScreen
import com.jagan.bustracking.ui.theme.Dark_Blue1
import com.jagan.bustracking.ui.theme.Dark_Blue1_Light
import com.jagan.bustracking.ui.theme.Dark_Blue2
import com.jagan.bustracking.ui.theme.Yellow1



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DriverDashboard() {
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = rememberScaffoldState(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Dark_Blue1)
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.buslogo),
                    tint = Color.White,
                    contentDescription = "app logo",
                    modifier = Modifier
                        .padding(start = 18.dp)
                        .size(25.dp)
                )
                Text(
                    text = "Driver Dashboard",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 15.dp),
                    color = Color.White
                )
            }
        },
        backgroundColor = Dark_Blue2,
        bottomBar = {

            val backStackEntry = navController.currentBackStackEntryAsState()
            val currentStack = backStackEntry.value?.destination?.route

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Dark_Blue1)
                    ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .weight(if ("update_screen" == currentStack) 2f else 1f)
                        .fillMaxSize()
                        .padding(start = 15.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if ("update_screen" == currentStack) Yellow1 else Dark_Blue1_Light)
                        .clickable {
                            navController.navigate(DriverScreen.UpdateScreen.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                                //popUpTo(0)
                                launchSingleTop = true
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center

                ) {
                    Icon(
                        Icons.Default.Edit,
                        "Update Details",
                        modifier = Modifier.size(20.dp),
                        tint = if ("update_screen" == currentStack) Color.White else Dark_Blue1
                    )

                    if ("update_screen" == currentStack) {
                        Text(
                            modifier = Modifier.padding(start = 7.dp),
                            text = "Update",
                            color = Color.White,
                            fontSize = 15.sp,
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .weight(if ("track_screen" == currentStack) 2f else 1f)
                        .fillMaxSize()
                        .padding(start = 10.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if ("track_screen" == currentStack) Yellow1 else Dark_Blue1_Light)
                        .clickable {
                            navController.navigate(DriverScreen.TrackScreen.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                                //popUpTo(0)
                                launchSingleTop = true
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        "Track Details",
                        modifier = Modifier.size(20.dp),
                        tint = if ("track_screen" == currentStack) Color.White else Dark_Blue1
                    )
                    if ("track_screen" == currentStack) {
                        Text(
                            modifier = Modifier.padding(start = 7.dp),
                            text = "Track",
                            color = Color.White,
                            fontSize = 15.sp,
                        )
                    }
                }
            }
        }
    ) {
        DriverNavGraph(navController)
    }
}
