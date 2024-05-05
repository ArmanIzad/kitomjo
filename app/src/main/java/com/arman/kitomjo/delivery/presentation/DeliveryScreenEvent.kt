package com.arman.kitomjo.delivery.presentation

import com.arman.kitomjo.delivery.presentation.entity.UIPackageEntity
import com.arman.kitomjo.delivery.presentation.entity.UIVehicleEntity

sealed interface DeliveryScreenEvent {
    data class AddVehicle(val vehicle: UIVehicleEntity) : DeliveryScreenEvent
    data class RemoveVehicle(val vehicleId: String) : DeliveryScreenEvent
    data class AddPackage(val deliveryPackage: UIPackageEntity) : DeliveryScreenEvent
    data class RemovePackage(val packageId: String) : DeliveryScreenEvent
    data class UpdateDeliveryBaseCost(val newCost: String?) : DeliveryScreenEvent
    data object CalculateTrips : DeliveryScreenEvent
}