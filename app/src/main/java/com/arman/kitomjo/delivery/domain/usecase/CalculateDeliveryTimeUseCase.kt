package com.arman.kitomjo.delivery.domain.usecase

import com.arman.kitomjo.delivery.domain.entity.AppException
import com.arman.kitomjo.delivery.domain.entity.PackageToDeliver
import com.arman.kitomjo.delivery.domain.entity.Trip
import com.arman.kitomjo.delivery.domain.entity.Vehicle
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.PriorityQueue

/**
 * This class is responsible for calculating the delivery times for a list of packages.
 */
class CalculateDeliveryTimeUseCase {
    /**
     * This function calculates the delivery times for a list of packages using a priority queue of vehicles.
     * @param packages The list of packages to be delivered.
     * @param vehicles The priority queue of vehicles available for delivery.
     * @return A list of trips, each containing the vehicle id and the packages delivered in that trip.
     */
    @Throws(AppException.NoAvailableVehicles::class)
    fun calculateDeliveryTimes(
        packages: List<PackageToDeliver>,
        vehicles: PriorityQueue<Vehicle>
    ): List<Trip> {
        val deliveryTimes = mutableListOf<Trip>()
        val packageTemp = packages.toMutableList()

        while (packageTemp.isNotEmpty()) {
            val vehicle = vehicles.poll()
            if (vehicle != null) {
                val maxSubset = findMaxWeights(packageTemp, vehicle.maxLoad)
                if (maxSubset.isEmpty() && vehicles.peek() == null) {
                    throw AppException.NoAvailableVehicles()
                }
                val deliveredPackages = maxSubset.sortedBy { it.deliveryDistance }
                var singleTrip = 0.0
                deliveredPackages.forEach { pkg ->
                    singleTrip = (pkg.deliveryDistance / vehicle.maxSpeed).roundDown()
                    pkg.deliveryTime = calculateTrips(vehicle.availableTime, singleTrip)
                }

                vehicle.availableTime = calculateTrips(vehicle.availableTime, singleTrip)
                vehicles.offer(vehicle)
                deliveryTimes.add(Trip(vehicle.id, deliveredPackages))

                packageTemp.removeAll(deliveredPackages)
            } else {
                break
            }
        }

        return deliveryTimes
    }

    /**
     * This function calculates the trip time taking into account round trips.
     * @param availableTime The available time for the vehicle.
     * @param singleTrip The time for a single trip.
     * @return The total trip time.
     */
    private fun calculateTrips(availableTime: Double, singleTrip: Double): Double {
        val roundTripTime = if (availableTime > 0.0) {
            availableTime.times(2)
        } else {
            0.0
        }
        return (BigDecimal.valueOf(roundTripTime) + BigDecimal.valueOf(singleTrip)).toDouble()
    }

    private fun Double.roundDown(decimalPoints: Int = 2): Double =
        BigDecimal(this).setScale(decimalPoints, RoundingMode.DOWN).toDouble()

    /**
     * This function finds the maximum weights that can be included in a delivery without exceeding the weight limit.
     * It prioritizes the number of packages that can be delivered in a single trip, then the higher weight and finally the shorter distance.
     * It uses dynamic programming to find the maximum weight that can be included in the delivery.
     * @param packages The list of packages to be delivered.
     * @param weightLimit The weight limit for the delivery.
     * @return The list of packages that can be included in the delivery.
     */
    private fun findMaxWeights(
        packages: List<PackageToDeliver>,
        weightLimit: Double
    ): List<PackageToDeliver> {
        val sortedPackages = packages.sortedWith(compareBy({ it.weight }, { it.deliveryDistance }))
        val numberOfPackages = sortedPackages.size
        val maxWeightMatrix = Array(numberOfPackages + 1) { DoubleArray(weightLimit.toInt() + 1) }
        val includedPackagesMatrix =
            Array(numberOfPackages + 1) { Array(weightLimit.toInt() + 1) { mutableListOf<PackageToDeliver>() } }

        for (i in 1 until numberOfPackages + 1) {
            for (w in 0 until weightLimit.toInt() + 1) {
                if (sortedPackages[i - 1].weight <= w && maxWeightMatrix[i - 1][w - sortedPackages[i - 1].weight.toInt()] + sortedPackages[i - 1].weight > maxWeightMatrix[i - 1][w]) {
                    maxWeightMatrix[i][w] =
                        maxWeightMatrix[i - 1][w - sortedPackages[i - 1].weight.toInt()] + sortedPackages[i - 1].weight
                    includedPackagesMatrix[i][w] =
                        mutableListOf(sortedPackages[i - 1]).apply { addAll(includedPackagesMatrix[i - 1][w - sortedPackages[i - 1].weight.toInt()]) }
                } else {
                    maxWeightMatrix[i][w] = maxWeightMatrix[i - 1][w]
                    includedPackagesMatrix[i][w] =
                        mutableListOf<PackageToDeliver>().apply { addAll(includedPackagesMatrix[i - 1][w]) }
                }
            }
        }

        var weightLimitInt = weightLimit.toInt()
        while (weightLimitInt > 0 && includedPackagesMatrix[numberOfPackages][weightLimitInt].isEmpty()) {
            weightLimitInt--
        }

        return includedPackagesMatrix[numberOfPackages][weightLimitInt]
    }
}