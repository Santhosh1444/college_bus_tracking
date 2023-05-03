package com.jagan.bustracking.student

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.jagan.bustracking.R
import com.jagan.bustracking.ui.theme.Dark_Blue1_Light
import com.jagan.bustracking.ui.theme.Dark_Blue2
import com.jagan.bustracking.ui.theme.Yellow1
import com.jagan.bustracking.util.BusDetails
import com.jagan.bustracking.util.SharedViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapScreen(
    paramBusNo:String
) {
    Scaffold(
        scaffoldState = rememberScaffoldState(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Dark_Blue2)
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.locationlogo),
                    tint = Color.White,
                    contentDescription = "app logo",
                    modifier = Modifier
                        .padding(start = 18.dp)
                        .size(30.dp)
                )
                Text(
                    text = "Bus Location.",
                    fontSize = 22.sp,
                    modifier = Modifier.padding(start = 15.dp),
                    color = Color.White
                )
            }
        }
    ) {

        val context = LocalContext.current
        var data by remember { mutableStateOf(BusDetails("NIL","NIL","NIL","12.00","80.10639","not active")) }

        SharedViewModel().getBusLiveData(context,paramBusNo) { busData ->
            data = busData
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSystemInDarkTheme()) Dark_Blue1_Light else Color.White)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                GoogleMapDivision(data)
            }

            val themeColorField = if (isSystemInDarkTheme()) Color.White else Color.Black
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 20.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = 2.dp,
                backgroundColor = if (isSystemInDarkTheme()) Dark_Blue2 else Color.White

            ) {
                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp, end = 35.dp, top = 15.dp, bottom = 15.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.buslogo),
                                tint = themeColorField,
                                contentDescription = "app logo",
                                modifier = Modifier
                                    .size(20.dp)
                            )
                            Text(
                                text = data.bus_no,
                                fontSize = 22.sp,
                                modifier = Modifier.padding(start = 10.dp),
                                color = themeColorField,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Driver: ")
                                }
                                withStyle(style = SpanStyle(fontSize = 14.sp, color = if (isSystemInDarkTheme()) Color.White else Color.Black)) {
                                    append(data.driver_name)
                                }
                            }
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = themeColorField,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Location: ")
                                }
                                withStyle(style = SpanStyle(fontSize = 14.sp, color = themeColorField)) {
                                    append((data.long)+" "+(data.lati))
                                }
                            }
                        )

                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = themeColorField,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Area: ")
                                }
                                withStyle(style = SpanStyle(fontSize = 14.sp, color = themeColorField)) {
                                    append(data.area)
                                }
                            }
                        )

                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    when (data.status) {
                                        "active" -> Color.Green
                                        "idle" -> Color.Yellow
                                        "problem" -> Color.Red
                                        "not active" -> Color.Blue
                                        else -> Color.Black
                                    }
                                )
                        )
                        Text(text = data.status, fontSize = 11.sp, color = themeColorField, modifier = Modifier.padding(top = 3.dp))
                    }

                }
            }
        }
    }
}

@Composable
fun GoogleMapDivision(data: BusDetails) {
    val location = LatLng(data.lati.toDouble(), data.long.toDouble())

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 5f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Circle(
                center = location,
                fillColor = Yellow1,
                radius = 2.0,
                strokeWidth = 1F,
            )
            Marker(
                state = MarkerState(position = location),
                title = data.area,
                snippet = "Marker in ${data.area}",
            )
        }
    }

}
