package com.jagan.bustracking.driver

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jagan.bustracking.R
import com.jagan.bustracking.ui.theme.Dark_Blue1
import com.jagan.bustracking.util.StoreData
import com.jagan.bustracking.util.StoreData.Companion.dataStoreBusNumber
import com.jagan.bustracking.util.StoreData.Companion.dataStoreDriverName
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UpdateScreen() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreData(context)

    // not editable bus number while in start
    val startStatus = dataStore.getStartedStatus.collectAsState(initial = "end")
    LaunchedEffect(key1 = startStatus.value ){
        if(startStatus.value.isEmpty() && startStatus.value.isBlank()){
            dataStore.saveStartStatus("end")
        }
    }

    //retrieve data
    val savedName = dataStore.getName.collectAsState(initial = "")
    val savedBus = dataStore.getBus.collectAsState(initial = "")

    val driverName = rememberSaveable { mutableStateOf(savedName.value) }
    val busNumber = rememberSaveable { mutableStateOf(savedBus.value) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = savedName.value, key2 = savedBus.value){
        driverName.value = savedName.value
        busNumber.value = savedBus.value

        dataStoreDriverName = driverName.value
        dataStoreBusNumber = busNumber.value
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
            text = "Update Your Credential",
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .height(230.dp)
                .width(300.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Dark_Blue1)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                value = driverName.value,
                onValueChange = {
                    driverName.value = it
                },
                label = {
                    Text(text = "driver name", fontSize = 18.sp, color = Color.Gray)
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Gray,
                    textColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            TextField(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                value = busNumber.value,
                onValueChange = {
                    if(startStatus.value == "end") {
                        busNumber.value = it
                    }else{
                        Toast.makeText(context,"Can't edit stop tracking and edit",Toast.LENGTH_SHORT).show()
                    }
                },
                label = {
                    Text(text = "bus number", fontSize = 18.sp, color = Color.Gray)

                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Gray,
                    textColor = Color.White

                ),
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    scope.launch {
                        dataStore.saveData(driverName.value,busNumber.value)
                    }
                    Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.width(100.dp),

                ) {
                Text(text = "UPDATE", color = Color.White, fontSize = 15.sp)
            }
        }
    }
}