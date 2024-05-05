package com.arman.kitomjo.delivery.domain.usecase

import com.arman.kitomjo.delivery.domain.entity.Delivery
import com.arman.kitomjo.delivery.domain.repository.OfferRepository
import javax.inject.Inject

/**
 * This class is responsible for calculating the cost of delivery after applying any applicable offers.
 *
 * @property deliveryCostStrategy The strategy used to calculate the base delivery cost.
 * @property offerStrategy The strategy used to determine if an offer is applicable.
 * @property offerRepository The repository used to fetch available offers.
 */
class CalculateOfferUseCase @Inject constructor(
    private val deliveryCostStrategy: DeliveryCostUseCase,
    private val offerStrategy: OfferUseCase,
    offerRepository: OfferRepository
) {
    private val offers = offerRepository.getOffers()

    /**
     * This function calculates the cost of delivery after applying any applicable offers.
     *
     * @param deliveryDetails The details of the delivery.
     * @return The cost of delivery after applying any applicable offers.
     */
    fun calculateCostAfterOffer(
        deliveryDetails: Delivery
    ): Double {
        val offer = offers.find { it.name == deliveryDetails.offerCode }
        return if (offer != null && offerStrategy.isApplicable(
                offer,
                deliveryDetails.offerCode,
                deliveryDetails.distance,
                deliveryDetails.weight
            )
        ) {
            val baseCost = deliveryCostStrategy.calculateDeliveryCost(deliveryDetails)
            baseCost - (baseCost * offer.percentage)
        } else {
            deliveryCostStrategy.calculateDeliveryCost(deliveryDetails)
        }
    }
}