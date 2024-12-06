package com.autoever.rightnow.models

data class Car(
    var manufacturer: String = "",
    var carType: String = "",
    var carPerson: Int = 1,
    var year: Int = 2024,
    var image: String = "",
    var carNumber: String = "",
    var pricePerHour: Long = 0,
    var carDescription: String = "",
    var carLocationLat: String = "",
    var carLocationLong: String = ""
)
