package com.arman.kitomjo.delivery.data

import com.arman.kitomjo.delivery.domain.entity.Offer
import com.arman.kitomjo.delivery.domain.repository.OfferRepository

/**
 * Fake implementation to return list of hardcoded offers
 */
class OfferRepositoryImpl : OfferRepository {

    private val offers = listOf(
        Offer("OFR001", 0.1, 201.0, null, 70.0, 200.0),
        Offer("OFR002", 0.07, 50.0, 150.0, 100.0, 250.0),
        Offer("OFR003", 0.05, 50.0, 250.0, 10.0, 150.0)
    )

    override fun getOffers(): List<Offer> = offers
}