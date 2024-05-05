package com.arman.kitomjo.delivery.presentation

import com.arman.kitomjo.delivery.domain.entity.AppException
import com.arman.kitomjo.delivery.domain.entity.PackageToDeliver
import com.arman.kitomjo.delivery.domain.entity.Trip
import com.arman.kitomjo.delivery.domain.usecase.CalculateDeliveryTimeUseCase
import com.arman.kitomjo.delivery.domain.usecase.CalculateOfferUseCase
import com.arman.kitomjo.delivery.presentation.entity.UIPackageEntity
import com.arman.kitomjo.delivery.presentation.entity.UIVehicleEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class DeliveryViewModelTest {

    private lateinit var viewModel: DeliveryViewModel

    @Mock
    private lateinit var calculateOfferUseCase: CalculateOfferUseCase

    @Mock
    private lateinit var calculateDeliveryTimeUseCase: CalculateDeliveryTimeUseCase

    @Before
    fun setup() {
        viewModel = DeliveryViewModel(calculateOfferUseCase, calculateDeliveryTimeUseCase)
    }

    @Test
    fun `addVehicle updates state with new vehicle`() = runTest {
        val vehicle = UIVehicleEntity("1", 100.0, 100.0)
        viewModel.onEvent(DeliveryScreenEvent.AddVehicle(vehicle))

        val state = viewModel.state.value
        assertEquals(vehicle, state.vehicles.first())
    }

    @Test
    fun `removeVehicle updates state without removed vehicle`() = runTest {
        val vehicle = UIVehicleEntity("1", 100.0, 100.0)
        viewModel.onEvent(DeliveryScreenEvent.AddVehicle(vehicle))
        viewModel.onEvent(DeliveryScreenEvent.RemoveVehicle(vehicle.id))

        val state = viewModel.state.value
        assertEquals(0, state.vehicles.size)
    }

    @Test
    fun `addPackage updates state with new package`() = runTest {
        val pkg = UIPackageEntity("1", 10.0, 10.0, "offer", 10.0, 10.0, 10.0)
        viewModel.onEvent(DeliveryScreenEvent.AddPackage(pkg))

        val state = viewModel.state.value
        assertEquals(pkg, state.packages.first())
    }

    @Test
    fun `removePackage updates state without removed package`() = runTest {
        val pkg = UIPackageEntity("1", 10.0, 10.0, "offer", 10.0, 10.0, 10.0)
        viewModel.onEvent(DeliveryScreenEvent.AddPackage(pkg))
        viewModel.onEvent(DeliveryScreenEvent.RemovePackage(pkg.id))

        val state = viewModel.state.value
        assertEquals(0, state.packages.size)
    }

    @Test
    fun `updateDeliveryBaseCost updates state with new base cost`() = runTest {
        val newCost = 20.0
        viewModel.onEvent(DeliveryScreenEvent.UpdateDeliveryBaseCost(newCost.toString()))

        val state = viewModel.state.value
        assertEquals(newCost, state.deliveryBaseCost)
    }

    @Test
    fun `calculateTrips updates state with new trips`() = runTest {
        val vehicle1 = UIVehicleEntity("1", 70.0, 200.0)
        viewModel.onEvent(DeliveryScreenEvent.AddVehicle(vehicle1))

        val baseDeliveryCost = 100.0
        val pkg1 = UIPackageEntity("pkg1", 50.0, 30.0, "OFR001", baseDeliveryCost)

        viewModel.onEvent(DeliveryScreenEvent.AddPackage(pkg1))

        val trips = listOf(Trip("1", listOf(PackageToDeliver("pkg1", 50.0, 30.0, "OFR001", 100.0))))

        whenever(
            calculateDeliveryTimeUseCase.calculateDeliveryTimes(
                any(),
                any()
            )
        ).thenReturn(trips)

        viewModel.onEvent(DeliveryScreenEvent.CalculateTrips)

        assertEquals(trips, viewModel.state.value.trips)
    }

    @Test
    fun `calculateTrips with no vehicle available`() = runTest {
        val vehicle1 = UIVehicleEntity("1", 70.0, 200.0)
        viewModel.onEvent(DeliveryScreenEvent.AddVehicle(vehicle1))

        val baseDeliveryCost = 100.0
        val pkg1 = UIPackageEntity("pkg1", 50.0, 30.0, "OFR001", baseDeliveryCost)

        viewModel.onEvent(DeliveryScreenEvent.AddPackage(pkg1))

        whenever(
            calculateDeliveryTimeUseCase.calculateDeliveryTimes(
                any(),
                any()
            )
        ).thenThrow(AppException.NoAvailableVehicles())

        viewModel.onEvent(DeliveryScreenEvent.CalculateTrips)

        assertEquals(emptyList<Trip>(), viewModel.state.value.trips)
        assert(viewModel.state.value.exception is AppException.NoAvailableVehicles)
    }
}