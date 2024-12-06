package com.autoever.rightnow.models

import java.io.Serializable


data class Car(
    val carType: String = "",
    val image: String = "",
    val carDescription: String = "",
    val carLocationLat: String = "",
    val carLocationLong: String = "",
    val carNumber: String = "",
    val carPerson: Int = 0,
    val manufacturer: String = "",
    val pricePerHour: Long = 0,
    val year: Int = 0
) : Serializable
