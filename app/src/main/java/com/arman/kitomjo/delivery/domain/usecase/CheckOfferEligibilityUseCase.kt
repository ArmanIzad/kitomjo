package com.arman.kitomjo.delivery.domain.usecase

import com.arman.kitomjo.delivery.domain.entity.Offer

interface OfferUseCase {
    /**
     * This method is used to check if an offer is applicable based on the offer details and delivery details.
     * @param offer The offer to be checked.
     * @param offerCode The code of the offer.
     * @param distance The distance of the delivery.
     * @param weight The weight of the delivery.
     * @return Boolean value indicating if the offer is applicable.
     */
    fun isApplicable(offer: Offer?, offerCode: String?, distance: Double, weight: Double): Boolean
}

object DefaultOfferStrategy : OfferUseCase {
    override fun isApplicable(
        offer: Offer?,
        offerCode: String?,
        distance: Double,
        weight: Double
    ): Boolean {
        return offer?.let {
            (it.name == offerCode) &&
                    (it.minDistance == null || distance >= it.minDistance) &&
                    (it.maxDistance == null || distance <= it.maxDistance) &&
                    (it.minWeight == null || weight >= it.minWeight) &&
                    (it.maxWeight == null || weight <= it.maxWeight)
        } ?: false
    }
}