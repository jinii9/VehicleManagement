package com.autoever.rightnow.models

data class RemoteControlStatus (
    val lock: Boolean = false,
    val power: Boolean = false,
    val returned: Boolean = false,
    val temperature: Int = 24,
    val temperaturePower: Boolean = false
)
