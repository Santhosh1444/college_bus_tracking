package com.jagan.bustracking.driver

import android.Manifest.permission.*
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.jagan.bustracking.backgroundlocation.hasLocationPermission
import com.jagan.bustracking.MainActivity.Companion.startLocationService
import com.jagan.bustracking.MainActivity.Companion.stopLocationService
import com.jagan.bustracking.R
import com.jagan.bustracking.backgroundlocation.LocationService.Companion.locationDetails
import com.jagan.bustracking.ui.theme.Dark_Blue1
import com.jagan.bustracking.ui.theme.Yellow1
import com.jagan.bustracking.util.SharedViewModel
import com.jagan.bustracking.util.StoreData
import com.jagan.bustracking.util.StoreData.Companion.dataStoreAlertStatus
import com.jagan.bustracking.util.StoreData.Companion.dataStoreBusNumber
import com.jagan.bustracking.util.StoreData.Companion.dataStoreDriverName
import kotlinx.coroutines.launch

@Composable
fun TrackScreen() {
    val locationDetailsUpdated = remember { locationDetails }
    val context = LocalContext.current
    val countOfPermission = rememberSaveable { mutableStateOf(0) }
    val locationManager =
        LocalContext.current.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // datastore

    val scope = rememberCoroutineScope()
    val dataStore = StoreData(context)
    val startStatus = dataStore.getStartedStatus.collectAsState(initial = "end")
    val alertStatus = dataStore.getAlertStatus.collectAsState(initial = "")

    LaunchedEffect(key1 = startStatus.value ){
        if(startStatus.value.isEmpty() && startStatus.value.isBlank()){
            dataStore.saveStartStatus("end")
        }
    }

    LaunchedEffect(key1 = alertStatus.value) {
        dataStoreAlertStatus = alertStatus.value
    }

    // permission
    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        countOfPermission.value = 0
        permission.entries.forEach { isGranted ->
            if (isGranted.value) {
                countOfPermission.value++
            }
        }

        if (countOfPermission.value != 2) {
            Toast.makeText(context, "Allow permission", Toast.LENGTH_LONG).show()
            val uri = Uri.fromParts("package", context.packageName, null)
            val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS, uri)
            startActivity(context, intent, null)
        }

    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Image(
            painter = painterResource(id = R.drawable.applogonobackground),
            contentDescription = "app logo",
            Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Start Sharing Bus Route",
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier
                .height(200.dp)
                .width(300.dp),
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Dark_Blue1
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 10.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .fillMaxWidth()
                        .background(if (startStatus.value == "end") Yellow1 else Color.Red)
                        .clickable {
                            // check the bus detail field are filled
                            if (dataStoreDriverName.isNotEmpty() && dataStoreBusNumber.isNotEmpty()) {

                                multiplePermissionsLauncher.launch(
                                    arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
                                )
                                if (context.hasLocationPermission()) {
                                    try {
                                        val isLocationEnabled =
                                            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                                        val isNetworkEnabled =
                                            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                                        if (isLocationEnabled && isNetworkEnabled) {
                                            // Location is enabled, perform your desired action here
                                            if (startStatus.value == "end") {
                                                startLocationService(context)
                                                scope.launch {
                                                    dataStore.saveStartStatus("start")
                                                }
                                                Toast
                                                    .makeText(
                                                        context,
                                                        "sharing started",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            } else {
                                                if (dataStoreAlertStatus == "alert") {
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "already in alert state",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                } else {
                                                    scope.launch {
                                                        dataStore.saveAlertStatus("alert")
                                                    }
                                                    SharedViewModel().updateAlertStatus(
                                                        context = context,
                                                        dataStoreBusNumber
                                                    )
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "Your are in alert stage please stay safe",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                }
                                            }
                                        } else if (!isLocationEnabled) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Location is off please turn on",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        } else {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Network is off please turn on",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    } catch (e: Exception) {
                                        Toast
                                            .makeText(
                                                context, "Some thing went wrong", Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                }
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        "fill driver details to start",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        }, contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (startStatus.value == "end") "Start Tracking" else if (alertStatus.value == "alert") "Alerting \uD83D\uDCE2" else "Alert ⚠️",
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 10.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .fillMaxWidth()
                        .background(Yellow1)
                        .clickable {
                            try {
                                if (startStatus.value == "start") {
                                    stopLocationService(context)
                                    scope.launch {
                                        dataStore.saveAlertStatus("")
                                    }
                                    scope.launch {
                                        dataStore.saveStartStatus("end")
                                    }
                                    Toast
                                        .makeText(context, "sharing stopped", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast
                                        .makeText(
                                            context,
                                            "tracking is not started",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            } catch (e: Exception) {
                                Toast
                                    .makeText(context, "Some thing went wrong", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }, contentAlignment = Alignment.Center
                ) {
                    Text(text = "Stop Tracking", color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = locationDetailsUpdated.value,
                    fontSize = 12.sp,
                    color = Color.White,
                )
            }
        }
    }
}
