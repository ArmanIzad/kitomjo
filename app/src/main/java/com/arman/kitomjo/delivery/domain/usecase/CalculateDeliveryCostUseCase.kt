package com.arman.kitomjo.delivery.domain.usecase

import com.arman.kitomjo.delivery.domain.entity.Delivery

interface DeliveryCostUseCase {
    /**
     * This method is used to calculate the delivery cost based on the delivery details.
     * @param deliveryDetails The details of the delivery.
     * @return The calculated cost of the delivery.
     */
    fun calculateDeliveryCost(deliveryDetails: Delivery): Double
}

object DefaultDeliveryCostStrategy : DeliveryCostUseCase {
    override fun calculateDeliveryCost(deliveryDetails: Delivery): Double {
        with(deliveryDetails) {
            return baseDeliveryCost + (distance * distanceMultiplier) + (weight * weightMultiplier)
        }
    }
}
