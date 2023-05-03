@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.jagan.bustracking.loginScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jagan.bustracking.R
import com.jagan.bustracking.ui.theme.Dark_Blue1
import com.jagan.bustracking.ui.theme.Yellow1
import com.jagan.bustracking.util.SharedViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navController: NavController
) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val userName = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val isDriverOrStudent = rememberSaveable {
        mutableStateOf(false)
    }
    val passwordVisible = rememberSaveable { mutableStateOf(false) }

    Image(
        painterResource(id = R.drawable.appbackground3), contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds,
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.applogo),
            contentDescription = "app logo",
            modifier = Modifier
                .size(240.dp)
                .fillMaxWidth()
        )


        Row {
            Button(
                onClick = {
                    isDriverOrStudent.value = false
                    userName.value = ""
                    password.value = ""
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.width(100.dp),
                colors =
                if (isDriverOrStudent.value)
                    ButtonDefaults.buttonColors(Dark_Blue1)
                else
                    ButtonDefaults.buttonColors(Yellow1)
            ) {
                Text(text = "Driver", color = Color.White, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.width(30.dp))

            Button(
                onClick = {
                    isDriverOrStudent.value = true
                    userName.value = ""
                    password.value = ""
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.width(100.dp),
                colors = if (!isDriverOrStudent.value)
                    ButtonDefaults.buttonColors(Dark_Blue1)
                else
                    ButtonDefaults.buttonColors(Yellow1)
            ) {
                Text(text = "Student", color = Color.White, fontSize = 15.sp)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            modifier = Modifier.padding(start = 50.dp, end = 50.dp),
            value = userName.value,
            onValueChange = {
                userName.value = it
            },
            label = {
                if (!isDriverOrStudent.value)
                    Text(text = "username", fontSize = 18.sp, color = Color.Gray)
                else
                    Text(text = "student id", fontSize = 18.sp, color = Color.Gray)
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Gray,
                textColor = Color.White

            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )

        Spacer(modifier = Modifier.height(10.dp))


        TextField(
            modifier = Modifier.padding(start = 50.dp, end = 50.dp),
            value = password.value,
            onValueChange = {
                password.value = it
            },
            singleLine = true,
            label = {
                Text(text = "password", fontSize = 18.sp, color = Color.Gray)
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Gray,
                textColor = Color.White

            ),
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible.value) painterResource(id = R.drawable.show)
                else painterResource(id = R.drawable.hide)
                val description = if (passwordVisible.value) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                    Icon(
                        painter = image,
                        description,
                        modifier = Modifier.size(25.dp),
                        tint = Color.White
                    )
                }
            },
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
        )
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = {
                if (userName.value.isNotEmpty() && password.value.isNotEmpty()) {
                    if (isDriverOrStudent.value) {
                        SharedViewModel().checkStudentUserName(
                            context = context,
                            username = userName.value,
                            password = password.value,
                            navController = navController
                        )
                    } else {
                        SharedViewModel().checkDriverUserName(
                            context = context,
                            username = userName.value,
                            password = password.value,
                            navController = navController
                        )
                    }
                } else {
                    Toast.makeText(context, "fill the above field", Toast.LENGTH_SHORT).show()
                }
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.width(100.dp),

            ) {
            Text(text = "Log in", color = Color.White, fontSize = 15.sp)
        }
    }
}
