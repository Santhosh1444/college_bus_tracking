package com.jagan.bustracking.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreData (private val context:Context) {
    companion object{
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore("Data")
        val DATA_KEY1 = stringPreferencesKey("name")
        val DATA_KEY2 = stringPreferencesKey("bus")
        val DATA_KEY3 = stringPreferencesKey("isStarted")
        val DATA_KEY4 = stringPreferencesKey("isAlerted")

        var dataStoreDriverName = ""
        var dataStoreBusNumber = ""
        var dataStoreAlertStatus = ""
    }

    val getName : Flow<String> = context.datastore.data.map {
        it[DATA_KEY1]?:""
    }
    val getBus : Flow<String> = context.datastore.data.map {
        it[DATA_KEY2]?:""
    }
    val getStartedStatus : Flow<String> = context.datastore.data.map {
        it[DATA_KEY3]?:""
    }
    val getAlertStatus : Flow<String> = context.datastore.data.map {
        it[DATA_KEY4]?:""
    }

    suspend fun saveAlertStatus(status:String){
        context.datastore.edit{
            it[DATA_KEY4] = status
        }
        dataStoreAlertStatus = status
    }

    suspend fun saveStartStatus(status:String){
        context.datastore.edit{
            it[DATA_KEY3] = status
        }
    }

    suspend fun saveData(name:String,bus:String){
        context.datastore.edit{
            it[DATA_KEY1] = name
            it[DATA_KEY2] = bus
        }
    }
}