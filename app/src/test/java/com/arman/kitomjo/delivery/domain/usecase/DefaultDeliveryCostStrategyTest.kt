package com.arman.kitomjo.delivery.domain.usecase

import com.arman.kitomjo.delivery.domain.entity.Delivery
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultDeliveryCostStrategyTest {

    @Test
    fun `calculateDeliveryCost returns correct cost`() {
        // Arrange
        val delivery = Delivery(
            baseDeliveryCost = 100.0,
            distance = 5.0,
            weight = 5.0,
            distanceMultiplier = 5.0,
            weightMultiplier = 10.0,
            offerCode = "OFR001"
        )

        // Act
        val result = DefaultDeliveryCostStrategy.calculateDeliveryCost(delivery)

        // Assert
        val expected = 100.0 + (5.0 * 5.0) + (5.0 * 10.0)
        assertEquals(expected, result, 0.001)
    }
}