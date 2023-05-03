package com.jagan.bustracking.util

data class BusDetails(
    var area: String = "",
    var bus_no:String = "",
    var driver_name: String = "",
    var lati: String = "",
    var long: String = "",
    var status: String = "",
)

data class LoginDetails(
    var password:String = "",
    var username:String = ""
)