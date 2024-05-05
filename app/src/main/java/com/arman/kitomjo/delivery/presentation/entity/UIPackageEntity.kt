package com.arman.kitomjo.delivery.presentation.entity

import java.util.UUID

data class UIPackageEntity(
    val id: String = UUID.randomUUID().toString(),
    val weight: Double,
    val deliveryDistance: Double,
    var offerCode: String?,
    var baseDeliveryCost: Double = 0.0,
    var deliveryTime: Double = 0.0,
    var price: Double = 0.0
)