package com.arman.kitomjo.delivery.domain.entity

data class Delivery(
    val distance: Double,
    val weight: Double,
    val offerCode: String?,
    val baseDeliveryCost: Double,
    val weightMultiplier: Double = 10.0,
    val distanceMultiplier: Double = 5.0
)