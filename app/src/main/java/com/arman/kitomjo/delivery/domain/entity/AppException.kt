package com.arman.kitomjo.delivery.domain.entity

import com.arman.kitomjo.R

sealed class AppException(val errorMessageResId: Int) : Exception() {
    class NoAvailableVehicles : AppException(R.string.error_no_available_vehicle)
    class InvalidBaseDeliveryCost : AppException(R.string.error_no_base_delivery_cost)
}