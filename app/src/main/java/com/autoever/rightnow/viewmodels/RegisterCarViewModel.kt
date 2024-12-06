package com.autoever.rightnow.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterCarViewModel : ViewModel() {
    var manufacturer: String = ""
    var carType: String = ""
    var carPerson: Int = 1
    var year: Int = 2024
    var image: String = ""

    var carNumber: String = ""
    var pricePerHour: Number = 0
    var carDescription: String = ""
    var carLocationLat: String = ""
    var carLocationLong: String = ""

}
