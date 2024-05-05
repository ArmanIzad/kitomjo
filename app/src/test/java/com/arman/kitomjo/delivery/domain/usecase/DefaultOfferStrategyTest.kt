package com.arman.kitomjo.delivery.domain.usecase

import com.arman.kitomjo.delivery.domain.entity.Offer
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultOfferStrategyTest {

    @Test
    fun `isApplicable returns true when offer matches and conditions are met`() {
        val offer = Offer("OFFER1", 10.0, 10.0, 50.0, 6.0, 100.0)
        assertTrue(DefaultOfferStrategy.isApplicable(offer, "OFFER1", 15.0, 7.0))
    }

    @Test
    fun `isApplicable returns true when offer matches and conditions are met, without minDistance`() {
        val offer = Offer("OFFER1", 10.0, null, 50.0, 6.0, 100.0)
        assertTrue(DefaultOfferStrategy.isApplicable(offer, "OFFER1", 15.0, 7.0))
    }

    @Test
    fun `isApplicable returns true when offer matches and conditions are met, without maxDistance`() {
        val offer = Offer("OFFER1", 10.0, 10.0, null, 6.0, 100.0)
        assertTrue(DefaultOfferStrategy.isApplicable(offer, "OFFER1", 15.0, 7.0))
    }

    @Test
    fun `isApplicable returns true when offer matches and conditions are met, without minWeight`() {
        val offer = Offer("OFFER1", 10.0, 10.0, 50.0, null, 100.0)
        assertTrue(DefaultOfferStrategy.isApplicable(offer, "OFFER1", 15.0, 7.0))
    }

    @Test
    fun `isApplicable returns true when offer matches and conditions are met, without maxWeight`() {
        val offer = Offer("OFFER1", 10.0, 10.0, 20.0, 6.0, null)
        assertTrue(DefaultOfferStrategy.isApplicable(offer, "OFFER1", 15.0, 7.0))
    }

    @Test
    fun `isApplicable returns false when offer code does not match`() {
        val offer = Offer("OFFER1", 10.0, 20.0, 5.0, 10.0, 100.0)
        assertFalse(DefaultOfferStrategy.isApplicable(offer, "OFFER2", 15.0, 7.0))
    }

    @Test
    fun `isApplicable returns false when distance is not within range`() {
        val offer = Offer("OFFER1", 10.0, 20.0, 5.0, 10.0, 100.0)
        assertFalse(DefaultOfferStrategy.isApplicable(offer, "OFFER1", 25.0, 7.0))
    }

    @Test
    fun `isApplicable returns false when distance is not within range, without maxDistance`() {
        val offer = Offer("OFFER1", 10.0, 20.0, null, 10.0, 100.0)
        assertFalse(DefaultOfferStrategy.isApplicable(offer, "OFFER1", 10.0, 7.0))
    }

    @Test
    fun `isApplicable returns false when distance is not within range, without minDistance`() {
        val offer = Offer("OFFER1", 10.0, null, 30.0, 10.0, 100.0)
        assertFalse(DefaultOfferStrategy.isApplicable(offer, "OFFER1", 50.0, 7.0))
    }

    @Test
    fun `isApplicable returns false when weight is not within range`() {
        val offer = Offer("OFFER1", 10.0, 20.0, 5.0, 10.0, 100.0)
        assertFalse(DefaultOfferStrategy.isApplicable(offer, "OFFER1", 15.0, 4.0))
    }

    @Test
    fun `isApplicable returns false when weight is not within range, without minWeight`() {
        val offer = Offer("OFFER1", 10.0, 20.0, 5.0, null, 100.0)
        assertFalse(DefaultOfferStrategy.isApplicable(offer, "OFFER1", 15.0, 200.0))
    }

    @Test
    fun `isApplicable returns false when weight is not within range, without maxWeight`() {
        val offer = Offer("OFFER1", 10.0, 20.0, 5.0, 10.0, null)
        assertFalse(DefaultOfferStrategy.isApplicable(offer, "OFFER1", 15.0, 4.0))
    }

    @Test
    fun `isApplicable returns false when offer is null`() {
        assertFalse(DefaultOfferStrategy.isApplicable(null, "OFFER1", 15.0, 7.0))
    }

    @Test
    fun `isApplicable returns false when offer code is null`() {
        val offer = Offer("OFFER1", 10.0, 20.0, 5.0, 10.0, 100.0)
        assertFalse(DefaultOfferStrategy.isApplicable(offer, null, 15.0, 7.0))
    }

    @Test
    fun `isApplicable returns false when offer and offer code is null`() {
        assertFalse(DefaultOfferStrategy.isApplicable(null, null, 15.0, 7.0))
    }
}