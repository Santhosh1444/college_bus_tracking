package com.jagan.bustracking.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jagan.bustracking.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun saveBusLocationDetails(data: BusDetails, context: Context) =
        CoroutineScope(Dispatchers.IO).launch {
            db.collection("bus").document(data.bus_no).get().addOnSuccessListener {
                val tempFireStoreRef = Firebase.firestore.collection("bus")

                tempFireStoreRef.document(data.bus_no)
                    .set(data)
                    .addOnFailureListener { exception ->
                        Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener { exception ->
                Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    fun updateAlertStatus(context:Context,documentName: String) =
        CoroutineScope(Dispatchers.IO).launch {
        val docRef = db.collection("bus").document(documentName)

        docRef.update("status", "problem")
            .addOnSuccessListener { Log.d("DEBUG", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show() }
    }

    private val items = MutableLiveData<List<BusDetails>>()

    fun getItems(): LiveData<List<BusDetails>> {
        return items
    }

    fun getBusDetails(context: Context) {
        db.collection("bus").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(context, "Some thing went wrong", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            val itemList = mutableListOf<BusDetails>()
            snapshot?.documents?.forEach { document ->
                val item = document.toObject(BusDetails::class.java)
                item?.let {
                    itemList.add(it)
                }
            }
            items.value = itemList
        }
    }

    fun getBusLiveData(
        context: Context,
        documentName: String,
        onBusDetailsChange: (BusDetails) -> Unit,
    ) = CoroutineScope(Dispatchers.IO).launch {
        db.collection("bus").document(documentName).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Toast.makeText(context, "Some thing went wrong", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val busData = snapshot.toObject(BusDetails::class.java) ?: BusDetails()
                onBusDetailsChange(busData)
            }
        }
    }

    fun checkStudentUserName(
        context: Context,
        username: String,
        password: String,
        navController: NavController
    ) = CoroutineScope(Dispatchers.IO).launch {
        db.collection("student").document(username).get()
            .addOnSuccessListener { snapshot ->
               if(snapshot!=null && snapshot.exists()){
                   val loginDetails = snapshot.toObject(LoginDetails:: class.java) ?: LoginDetails()
                   if(loginDetails.password == password){
                       Toast.makeText(context,"Welcome !!",Toast.LENGTH_SHORT).show()
                       navController.navigate(Screen.StudentDashboard.route)
                   }else{
                       Toast.makeText(context,"password incorrect",Toast.LENGTH_SHORT).show()
                   }
               }else{
                   Toast.makeText(context,"username not found",Toast.LENGTH_SHORT).show()
               }
            }.addOnFailureListener { exception ->
                Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }
    fun checkDriverUserName(
        context: Context,
        username: String,
        password: String,
        navController: NavController
    ) = CoroutineScope(Dispatchers.IO).launch {
        db.collection("driver").document(username).get()
            .addOnSuccessListener { snapshot ->
               if(snapshot!=null && snapshot.exists()){
                   val loginDetails = snapshot.toObject(LoginDetails:: class.java) ?: LoginDetails()
                   if(loginDetails.password == password){
                       Toast.makeText(context,"Welcome !!",Toast.LENGTH_SHORT).show()
                       navController.navigate(Screen.DriverDashboard.route)
                   }else{
                       Toast.makeText(context,"password incorrect",Toast.LENGTH_SHORT).show()
                   }
               }else{
                   Toast.makeText(context,"username not found",Toast.LENGTH_SHORT).show()
               }
            }.addOnFailureListener { exception ->
                Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}



