package com.arman.kitomjo.delivery.domain.entity

data class Vehicle(
    val id: String,
    val maxLoad: Double,
    val maxSpeed: Double,
    var availableTime: Double = 0.0
) : Comparable<Vehicle> {
    override fun compareTo(other: Vehicle): Int {
        return compareValuesBy(this, other, { it.availableTime }, { it.maxLoad })
    }
}