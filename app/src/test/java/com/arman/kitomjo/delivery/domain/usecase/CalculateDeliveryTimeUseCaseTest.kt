package com.arman.kitomjo.delivery.domain.usecase

import com.arman.kitomjo.delivery.domain.entity.AppException
import com.arman.kitomjo.delivery.domain.entity.PackageToDeliver
import com.arman.kitomjo.delivery.domain.entity.Trip
import com.arman.kitomjo.delivery.domain.entity.Vehicle
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.PriorityQueue

class CalculateDeliveryTimeUseCaseTest {

    private val calculateDeliveryTimeUseCase = CalculateDeliveryTimeUseCase()

    @Test
    fun `calculateDeliveryTimes returns correct times for multiple packages and vehicles`() {
        val packages = listOf(
            PackageToDeliver("1", 50.0, 30.0),
            PackageToDeliver("2", 75.0, 125.0),
            PackageToDeliver("3", 175.0, 100.0),
            PackageToDeliver("4", 110.0, 60.0),
            PackageToDeliver("5", 155.0, 95.0)
        )
        val vehicles = PriorityQueue<Vehicle>().apply {
            add(Vehicle("1", 200.0, 70.0, 0.0))
            add(Vehicle("2", 200.0, 70.0, 0.0))
        }

        val result = calculateDeliveryTimeUseCase.calculateDeliveryTimes(packages, vehicles)

        assertEquals(
            listOf(
                Trip(
                    "1",
                    listOf(
                        PackageToDeliver("4", 110.0, 60.0, null, 0.85),
                        PackageToDeliver("2", 75.0, 125.0, null, 1.78)
                    )
                ),
                Trip("2", listOf(PackageToDeliver("3", 175.0, 100.0, null, 1.42))),
                Trip("2", listOf(PackageToDeliver("5", 155.0, 95.0, null, 4.19))),
                Trip("1", listOf(PackageToDeliver("1", 50.0, 30.0, null, 3.98)))
            ),
            result
        )
    }

    @Test
    fun `calculateDeliveryTimes returns correct times for multiple packages and vehicles scenario 2`() {
        val packages = listOf(
            PackageToDeliver("1", 50.0, 30.0),
            PackageToDeliver("2", 75.0, 125.0),
            PackageToDeliver("3", 175.0, 100.0),
            PackageToDeliver("4", 110.0, 60.0),
            PackageToDeliver("5", 110.0, 10.0),
            PackageToDeliver("6", 155.0, 95.0)
        )
        val vehicles = PriorityQueue<Vehicle>().apply {
            add(Vehicle("1", 50.0, 70.0, 0.0))
            add(Vehicle("2", 200.0, 70.0, 0.0))
            add(Vehicle("3", 300.0, 70.0, 0.0))
        }

        val result = calculateDeliveryTimeUseCase.calculateDeliveryTimes(packages, vehicles)

        assertEquals(
            listOf(
                Trip("1", listOf(PackageToDeliver("1", 50.0, 30.0, null, 0.42))),
                Trip(
                    "2",
                    listOf(
                        PackageToDeliver("5", 110.0, 10.0, null, 0.14),
                        PackageToDeliver("2", 75.0, 125.0, null, 1.78)
                    )
                ),
                Trip(
                    "3",
                    listOf(
                        PackageToDeliver("4", 110.0, 60.0, null, 0.85),
                        PackageToDeliver("3", 175.0, 100.0, null, 1.42)
                    )
                ),
                Trip("1", listOf()),
                Trip("1", listOf()),
                Trip("3", listOf(PackageToDeliver("6", 155.0, 95.0, null, 4.19)))
            ),
            result
        )
    }

    @Test
    fun `calculateDeliveryTimes returns correct times for multiple packages and vehicles scenario 3`() {
        val packages = listOf(
            PackageToDeliver("1", 50.0, 300.0),
            PackageToDeliver("2", 75.0, 125.0),
            PackageToDeliver("3", 175.0, 1000.0),
            PackageToDeliver("4", 250.0, 60.0),
            PackageToDeliver("5", 110.0, 10.0),
            PackageToDeliver("6", 110.0, 10.0),
            PackageToDeliver("7", 110.0, 10.0),
            PackageToDeliver("8", 155.0, 95.0)
        )
        val vehicles = PriorityQueue<Vehicle>().apply {
            add(Vehicle("1", 500.0, 70.0, 0.0))
            add(Vehicle("2", 200.0, 70.0, 0.0))
            add(Vehicle("3", 300.0, 70.0, 0.0))
            add(Vehicle("4", 100.0, 70.0, 0.0))
        }

        val result = calculateDeliveryTimeUseCase.calculateDeliveryTimes(packages, vehicles)

        assertEquals(
            listOf(
                Trip("4", listOf(PackageToDeliver("2", 75.0, 125.0, null, 1.78))),
                Trip("2", listOf(PackageToDeliver("3", 175.0, 1000.0, null, 14.28))),
                Trip(
                    "3",
                    listOf(
                        PackageToDeliver("4", 250.0, 60.0, null, 0.85),
                        PackageToDeliver("1", 50.0, 300.0, null, 4.28)
                    )
                ),
                Trip(
                    "1", listOf(
                        PackageToDeliver("7", 110.0, 10.0, null, 0.14),
                        PackageToDeliver("6", 110.0, 10.0, null, 0.14),
                        PackageToDeliver("5", 110.0, 10.0, null, 0.14),
                        PackageToDeliver("8", 155.0, 95.0, null, 1.35)
                    )
                )
            ),
            result
        )
    }

    @Test
    fun `calculateDeliveryTimes returns correct times for single package and vehicle`() {
        val packages = listOf(PackageToDeliver("1", 1.0, 10.0))
        val vehicles = PriorityQueue<Vehicle>().apply {
            add(Vehicle("1", 20.0, 10.0, 0.0))
        }

        val result = calculateDeliveryTimeUseCase.calculateDeliveryTimes(packages, vehicles)

        assertEquals(listOf(Trip("1", listOf(PackageToDeliver("1", 1.0, 10.0, null, 1.0)))), result)
    }

    @Test
    fun `calculateDeliveryTimes returns empty list for no packages`() {
        val packages = emptyList<PackageToDeliver>()
        val vehicles = PriorityQueue<Vehicle>().apply {
            add(Vehicle("1", 20.0, 10.0, 0.0))
        }

        val result = calculateDeliveryTimeUseCase.calculateDeliveryTimes(packages, vehicles)

        assertEquals(emptyList<Pair<String, Double>>(), result)
    }

    @Test
    fun `calculateDeliveryTimes returns empty list for no vehicles`() {
        val packages = listOf(
            PackageToDeliver("1", 1.0, 10.0),
            PackageToDeliver("2", 2.0, 5.0),
            PackageToDeliver("3", 3.0, 15.0)
        )
        val vehicles = PriorityQueue<Vehicle>()

        val result = calculateDeliveryTimeUseCase.calculateDeliveryTimes(packages, vehicles)

        assertEquals(emptyList<Pair<String, Double>>(), result)
    }

    @Test(expected = AppException.NoAvailableVehicles::class)
    fun `calculateDeliveryTimes when_no_vehicle_is_available`() {
        val packages = listOf(
            PackageToDeliver("1", 100.0, 100.0),
        )
        val vehicles = PriorityQueue<Vehicle>().apply {
            add(Vehicle("1", 10.0, 10.0, 0.0))
        }

        val result = calculateDeliveryTimeUseCase.calculateDeliveryTimes(packages, vehicles)

        assertEquals(emptyList<Pair<String, Double>>(), result)
    }
}