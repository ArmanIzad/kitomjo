package com.arman.kitomjo.delivery.presentation

import com.arman.kitomjo.delivery.domain.entity.AppException
import com.arman.kitomjo.delivery.domain.entity.Trip
import com.arman.kitomjo.delivery.presentation.entity.UIPackageEntity
import com.arman.kitomjo.delivery.presentation.entity.UIVehicleEntity

data class DeliveryScreenState(
    val deliveryBaseCost: Double? = 100.0,
    val vehicles: List<UIVehicleEntity> = listOf(),
    val packages: List<UIPackageEntity> = listOf(),
    val trips: List<Trip> = listOf(),
    val exception: AppException? = null
)