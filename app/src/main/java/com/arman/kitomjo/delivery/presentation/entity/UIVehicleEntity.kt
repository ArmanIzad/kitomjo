package com.arman.kitomjo.delivery.presentation.entity

import java.util.UUID

data class UIVehicleEntity(
    val id: String = UUID.randomUUID().toString(),
    val maxSpeed: Double,
    val maxLoad: Double
)