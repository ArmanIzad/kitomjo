package com.arman.kitomjo.delivery.domain.usecase

import com.arman.kitomjo.delivery.domain.entity.Delivery
import com.arman.kitomjo.delivery.domain.entity.Offer
import com.arman.kitomjo.delivery.domain.repository.OfferRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any

@RunWith(MockitoJUnitRunner::class)
class CalculateOfferUseCaseTest {

    @Mock
    private lateinit var deliveryCostStrategy: DeliveryCostUseCase

    @Mock
    private lateinit var offerStrategy: OfferUseCase

    @Mock
    private lateinit var offerRepository: OfferRepository

    private lateinit var calculateOfferUseCase: CalculateOfferUseCase

    @Test
    fun `calculateCostAfterOffer returns discounted cost when offer is applicable`() {
        val delivery = Delivery(5.0, 5.0, "OFFER1", 100.0, 10.0, 5.0)
        `when`(offerStrategy.isApplicable(any(), any(), any(), any())).thenReturn(true)
        `when`(deliveryCostStrategy.calculateDeliveryCost(any())).thenReturn(100.0)
        `when`(offerRepository.getOffers()).thenReturn(
            listOf(
                Offer(
                    "OFFER1",
                    0.1,
                    1.0,
                    10.0,
                    1.0,
                    50.0
                )
            )
        )
        calculateOfferUseCase =
            CalculateOfferUseCase(deliveryCostStrategy, offerStrategy, offerRepository)

        val result = calculateOfferUseCase.calculateCostAfterOffer(delivery)

        assertEquals(90.0, result, 0.0)
    }

    @Test
    fun `calculateCostAfterOffer returns base cost when offer is not applicable`() {
        val delivery = Delivery(5.0, 5.0, "OFFER1", 100.0, 10.0, 5.0)
        `when`(offerStrategy.isApplicable(any(), any(), any(), any())).thenReturn(false)
        `when`(deliveryCostStrategy.calculateDeliveryCost(any())).thenReturn(100.0)
        `when`(offerRepository.getOffers()).thenReturn(
            listOf(
                Offer(
                    "OFFER1",
                    10.0,
                    20.0,
                    5.0,
                    10.0,
                    50.0
                )
            )
        )
        calculateOfferUseCase =
            CalculateOfferUseCase(deliveryCostStrategy, offerStrategy, offerRepository)

        val result = calculateOfferUseCase.calculateCostAfterOffer(delivery)

        assertEquals(100.0, result, 0.0)
    }
}