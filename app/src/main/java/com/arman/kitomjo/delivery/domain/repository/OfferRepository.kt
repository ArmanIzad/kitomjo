package com.arman.kitomjo.delivery.domain.repository

import com.arman.kitomjo.delivery.domain.entity.Offer

interface OfferRepository {
    /**
     * This method is used to get a list of all offers.
     * @return List of Offer objects.
     */
    fun getOffers(): List<Offer>
}