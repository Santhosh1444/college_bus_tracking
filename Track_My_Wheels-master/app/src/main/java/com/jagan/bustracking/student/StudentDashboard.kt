package com.jagan.bustracking.student

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jagan.bustracking.R
import com.jagan.bustracking.ui.theme.Dark_Blue1
import com.jagan.bustracking.ui.theme.Dark_Blue1_Light
import com.jagan.bustracking.ui.theme.Dark_Blue2
import com.jagan.bustracking.ui.theme.Yellow1
import com.jagan.bustracking.util.SharedViewModel
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StudentDashboard(
    navController: NavController
) {
    val isPending = rememberSaveable() { mutableStateOf(true) }

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
                    painter = painterResource(id = R.drawable.buslogo),
                    tint = Color.White,
                    contentDescription = "app logo",
                    modifier = Modifier
                        .padding(start = 18.dp)
                        .size(30.dp)
                )
                Text(
                    text = "Student Dashboard",
                    fontSize = 22.sp,
                    modifier = Modifier.padding(start = 15.dp),
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                if (isPending.value) {
                    CircularProgressIndicator(
                        color = Color.Green,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(top = 2.dp, start = 2.dp),
                        strokeWidth = 3.dp
                    )
                }
            }
        }
    ) {

        // data retrieve example
        val context = LocalContext.current
        val sharedViewModel = SharedViewModel()
        val list by sharedViewModel.getItems().observeAsState(emptyList())
        val search = rememberSaveable { mutableStateOf("") }

        LaunchedEffect(sharedViewModel) {
            sharedViewModel.getBusDetails(context)
        }

        val keyboardController = LocalSoftwareKeyboardController.current

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSystemInDarkTheme()) Dark_Blue1_Light else Color.White)
        ) {
            itemsIndexed(
                list
            ) { idx, data ->
                if (idx == 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(53.dp)
                                .padding(start = 15.dp, end = 15.dp)
                                .clip(
                                    RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp)
                                ),
                            value = search.value,
                            onValueChange = {
                                if (it.length <= 20) search.value = it
                            },
                            placeholder = {
                                Text(
                                    text = "Search bus by no. | driver | area",
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = Color.White,
                                disabledTextColor = Color.Transparent,
                                backgroundColor = Dark_Blue1,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                            trailingIcon = {
                                IconButton(
                                    onClick = { keyboardController?.hide() }
                                ) {
                                    Icon(
                                        Icons.Filled.Search,
                                        contentDescription = "Search",
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                        )
                    }
                }

                if (
                    (search.value.trim().uppercase()) in data.bus_no.uppercase() ||
                    (search.value.trim().uppercase()) in data.area.uppercase() ||
                    (search.value.trim().uppercase()) in data.driver_name.uppercase()
                ) {

                    CardForBusList(
                        name = data.bus_no,
                        location = data.area,
                        status = data.status,
                        driver = data.driver_name,
                        navController = navController
                    )

                }

                isPending.value = false

                Spacer(modifier = Modifier.height(10.dp))
                if (idx == list.size - 1) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun CardForBusList(
    name: String,
    location: String,
    status: String,
    driver: String,
    navController: NavController
) {
    Card(
        elevation = 9.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(start = 15.dp, end = 15.dp),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = if (isSystemInDarkTheme()) Dark_Blue1_Light else Color.White
    ) {
        Row(
            modifier = Modifier
                .background(Dark_Blue1)
                .clickable {
                    navController.navigate("map_screen/$name")
                }
        ) {
            Box(
                modifier = Modifier
                    .background(Dark_Blue2)
                    .weight(.9f)
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.buslogo),
                    tint = Color.White,
                    contentDescription = "app logo",
                    modifier = Modifier
                        .padding(18.dp)
                        .size(50.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Bus $name",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Dark_Blue2
                )

                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Dark_Blue1,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Driver: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 13.sp, color = Dark_Blue1)) {
                            append(driver)
                        }
                    }
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Dark_Blue1,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Location: ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontStyle = FontStyle.Italic,
                                fontSize = 12.sp,
                                color = Dark_Blue1
                            )
                        ) {
                            append(location)
                        }
                    }

                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(.6f)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                when (status) {
                                    "active" -> Color.Green
                                    "idle" -> Color.Yellow
                                    "problem" -> Color.Red
                                    "not active" -> Color.Blue
                                    else -> Color.Black
                                }
                            )
                    )
                    Text(text = status, fontSize = 10.sp, color = Color.Black)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(.1f)
                    .background(Yellow1)
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(.1f)
                    .background(Dark_Blue2)
            )
        }
    }
}