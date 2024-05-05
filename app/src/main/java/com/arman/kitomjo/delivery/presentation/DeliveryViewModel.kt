package com.arman.kitomjo.delivery.presentation

import androidx.lifecycle.ViewModel
import com.arman.kitomjo.delivery.domain.entity.AppException
import com.arman.kitomjo.delivery.domain.entity.Delivery
import com.arman.kitomjo.delivery.domain.entity.PackageToDeliver
import com.arman.kitomjo.delivery.domain.entity.Trip
import com.arman.kitomjo.delivery.domain.entity.Vehicle
import com.arman.kitomjo.delivery.domain.usecase.CalculateDeliveryTimeUseCase
import com.arman.kitomjo.delivery.domain.usecase.CalculateOfferUseCase
import com.arman.kitomjo.delivery.presentation.entity.UIPackageEntity
import com.arman.kitomjo.delivery.presentation.entity.toUIEntity
import com.arman.kitomjo.delivery.presentation.entity.toVehicle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.PriorityQueue
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(
    private val calculateOffer: CalculateOfferUseCase,
    private val calculateDeliveryTimeUseCase: CalculateDeliveryTimeUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<DeliveryScreenState> =
        MutableStateFlow(DeliveryScreenState())
    val state = _state.asStateFlow()

    fun onEvent(event: DeliveryScreenEvent) {
        when (event) {
            is DeliveryScreenEvent.AddVehicle -> {
                _state.update {
                    it.copy(
                        vehicles = it.vehicles.toMutableList().apply {
                            add(event.vehicle)
                        },
                        trips = listOf()
                    )
                }
            }

            is DeliveryScreenEvent.RemoveVehicle -> {
                _state.update {
                    it.copy(
                        vehicles = it.vehicles.toMutableList().apply {
                            removeAll { vehicle -> vehicle.id == event.vehicleId }
                        },
                        trips = listOf()
                    )
                }
            }

            is DeliveryScreenEvent.AddPackage -> {
                _state.update {
                    it.copy(
                        packages = it.packages.toMutableList().apply {
                            add(calculateOffer(event.deliveryPackage))
                        },
                        trips = listOf()
                    )
                }
            }

            is DeliveryScreenEvent.RemovePackage -> {
                _state.update {
                    it.copy(
                        packages = it.packages.toMutableList().apply {
                            removeAll { pkg -> pkg.id == event.packageId }
                        },
                        trips = listOf()
                    )
                }
            }

            is DeliveryScreenEvent.UpdateDeliveryBaseCost -> {
                val costDouble = event.newCost?.toDoubleOrNull()
                if (costDouble == null || costDouble <= 0.0) {
                    handleExceptions(AppException.InvalidBaseDeliveryCost())
                } else {
                    handleExceptions(null)
                    updateBaseDeliveryCost(costDouble)
                }
                _state.update {
                    it.copy(
                        deliveryBaseCost = costDouble
                    )
                }
            }

            is DeliveryScreenEvent.CalculateTrips -> {
                updateTrips()
            }
        }
    }

    private fun calculateOffer(pkg: UIPackageEntity): UIPackageEntity {
        val deliveryDetails =
            Delivery(pkg.deliveryDistance, pkg.weight, pkg.offerCode, pkg.baseDeliveryCost)
        pkg.price = calculateOffer.calculateCostAfterOffer(deliveryDetails)
        pkg.offerCode = deliveryDetails.offerCode
        return pkg
    }

    private fun calculateTrips(
        deliveryPackages: List<PackageToDeliver>,
        vehicles: PriorityQueue<Vehicle>
    ): List<Trip> {
        var result: List<Trip> = listOf()
        try {
            result = calculateDeliveryTimeUseCase.calculateDeliveryTimes(deliveryPackages, vehicles)
            handleExceptions(null)
        } catch (ex: AppException) {
            handleExceptions(ex)
        }

        return result
    }

    private fun updateBaseDeliveryCost(newCost: Double) {
        updateBaseDeliveryCostsAndRecalculate(newCost)
        updateTrips()
    }

    private fun updateBaseDeliveryCostsAndRecalculate(newCost: Double) {
        val updatedPackages = _state.value.packages.map { pkg ->
            pkg.baseDeliveryCost = newCost
            calculateOffer(pkg)
        }
        _state.update {
            it.copy(
                packages = updatedPackages
            )
        }
    }

    private fun updateTrips() {
        val deliveryPackages = _state.value.packages.map { pkg ->
            PackageToDeliver(
                pkg.id,
                pkg.weight,
                pkg.deliveryDistance,
                pkg.offerCode,
                pkg.deliveryTime,
                pkg.price
            )
        }
        val vehicles = PriorityQueue(_state.value.vehicles.map { it.toVehicle() })
        val trips = calculateTrips(deliveryPackages, vehicles)

        val packages = trips.map { it.packages }.flatten()

        if (packages.isEmpty()) {
            _state.update { deliveryScreenState ->
                deliveryScreenState.copy(
                    trips = trips
                )
            }
        } else {
            _state.update { deliveryScreenState ->
                deliveryScreenState.copy(
                    trips = trips,
                    packages = deliveryScreenState.packages.map { pkg ->
                        val updatedPkg = packages.find { it.id == pkg.id }
                        if (updatedPkg != null) {
                            pkg.deliveryTime = updatedPkg.deliveryTime
                        }
                        pkg
                    }
                )
            }
        }
    }

    private fun handleExceptions(ex: AppException?) {
        when (ex) {
            is AppException.NoAvailableVehicles -> {
                _state.update {
                    it.copy(
                        exception = ex
                    )
                }
            }

            is AppException.InvalidBaseDeliveryCost -> {
                _state.update {
                    it.copy(
                        exception = ex
                    )
                }
            }

            null -> {
                _state.update {
                    it.copy(
                        exception = null
                    )
                }
            }
        }
    }
}