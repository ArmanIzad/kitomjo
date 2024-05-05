package com.arman.kitomjo.delivery.presentation.entity

import com.arman.kitomjo.delivery.domain.entity.PackageToDeliver
import com.arman.kitomjo.delivery.domain.entity.Vehicle

fun UIVehicleEntity.toVehicle() = Vehicle(
    id = id,
    maxSpeed = maxSpeed,
    maxLoad = maxLoad
)

fun PackageToDeliver.toUIEntity() = UIPackageEntity(
    id = id,
    weight = weight,
    deliveryDistance = deliveryDistance,
    offerCode = offerCode,
    price = price,
    deliveryTime = deliveryTime
)