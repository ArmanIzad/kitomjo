package com.arman.kitomjo.delivery.domain.entity

data class Offer(
    val name: String,
    val percentage: Double,
    val minDistance: Double?,
    val maxDistance: Double?,
    val minWeight: Double?,
    val maxWeight: Double?
)
