package com.arman.kitomjo.delivery.domain.entity

data class Trip(val vehicleId: String, val packages: List<PackageToDeliver>)