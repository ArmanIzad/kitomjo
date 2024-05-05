package com.arman.kitomjo.delivery.domain.entity

data class PackageToDeliver(
    val id: String,
    val weight: Double,
    val deliveryDistance: Double,
    val offerCode: String?  = null,
    var deliveryTime: Double = 0.0,
    var price: Double = 0.0
)