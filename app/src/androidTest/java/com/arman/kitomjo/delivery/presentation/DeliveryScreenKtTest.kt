package com.arman.kitomjo.delivery.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.espresso.Espresso
import com.arman.kitomjo.MainActivity
import com.arman.kitomjo.R
import com.arman.kitomjo.assertEditableValue
import com.arman.kitomjo.getEditableTextFromNode
import com.arman.kitomjo.tryScrollToNode
import com.arman.kitomjo.waitUntilExists
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DeliveryScreenKtTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_default_screen_elements() {
        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.cd_base_delivery_cost)
            )
            .fetchSemanticsNode()
            .getEditableTextFromNode()
            .let { assert(it == "100.0") }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.cd_add_vehicle)
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.cd_add_package)
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.cd_calculate_trips)
            )
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun test_error_base_delivery_cost() {
        val baseDeliveryNode = composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.cd_base_delivery_cost)
            )

        baseDeliveryNode
            .fetchSemanticsNode()
            .getEditableTextFromNode()
            .let { assert(it == "100.0") }

        val value = "0.0"

        baseDeliveryNode.performTextReplacement(value)

        baseDeliveryNode
            .fetchSemanticsNode()
            .getEditableTextFromNode()
            .let { assert(it == value) }

        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.resources.getString(R.string.cd_base_delivery_cost_error)
        ).assertIsDisplayed()
    }

    @Test
    fun test_add_remove_vehicle() {
        addVehicle("80", "120")
        removeVehicle(1)
    }

    @Test
    fun test_add_remove_package() {
        addPackage("50", "50", "OFF001")
        removePackage(1)
    }

    @Test
    fun test_calculate_trips_success() {
        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.cd_base_delivery_cost)
            )
            .assertIsDisplayed()

        clickButton(
            composeTestRule.activity.resources.getString(R.string.cd_add_vehicle)
        )

        composeTestRule.waitUntilExists(
            hasContentDescription(
                composeTestRule.activity.resources.getString(
                    R.string.cd_vehicle_max_load
                )
            )
        )

        addVehicle("80", "120")

        addPackage("50", "50", "OFF001")

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.cd_calculate_trips)
            )
            .assertIsDisplayed()
            .assertIsEnabled()

        clickButton(
            composeTestRule.activity.resources.getString(R.string.cd_calculate_trips)
        )

        composeTestRule.waitUntilExists(
            hasContentDescription(
                composeTestRule.activity.resources.getString(
                    R.string.cd_trips_row, 1
                )
            )
        )
    }

    @Test
    fun test_calculate_trips_error() {
        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.cd_base_delivery_cost)
            )
            .assertIsDisplayed()

        clickButton(
            composeTestRule.activity.resources.getString(R.string.cd_add_vehicle)
        )

        composeTestRule.waitUntilExists(
            hasContentDescription(
                composeTestRule.activity.resources.getString(
                    R.string.cd_vehicle_max_load
                )
            )
        )

        addVehicle("1", "1")

        addPackage("50", "50", "OFF001")

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.cd_calculate_trips)
            )
            .assertIsDisplayed()
            .assertIsEnabled()

        clickButton(
            composeTestRule.activity.resources.getString(R.string.cd_calculate_trips)
        )

        composeTestRule.waitUntilExists(
            hasContentDescription(
                composeTestRule.activity.resources.getString(
                    R.string.cd_trips_error
                )
            )
        )
    }

    private fun addVehicle(maxSpeed: String, maxLoad: String) {
        clickButton(
            composeTestRule.activity.resources.getString(R.string.cd_add_vehicle)
        )

        composeTestRule.waitUntilExists(
            hasContentDescription(
                composeTestRule.activity.resources.getString(
                    R.string.cd_vehicle_max_speed
                )
            )
        )

        addValueToEditable(
            composeTestRule.activity.resources.getString(
                R.string.cd_vehicle_max_speed
            ), maxSpeed
        )

        addValueToEditable(
            composeTestRule.activity.resources.getString(
                R.string.cd_vehicle_max_load
            ), maxLoad
        )

        Espresso.closeSoftKeyboard()

        clickButton(
            composeTestRule.activity.resources.getString(R.string.cd_confirm_button)
        )

        composeTestRule.waitUntilExists(
            hasContentDescription(
                composeTestRule.activity.resources.getString(
                    R.string.cd_vehicle_row, 1
                )
            )
        )
    }

    // Starts from one
    private fun removeVehicle(index: Int) {
        swipeToRemove(
            composeTestRule.activity.resources.getString(
                R.string.cd_vehicle_row, index
            )
        )
    }

    private fun addPackage(weight: String, deliveryDistance: String, offerCode: String) {
        clickButton(
            composeTestRule.activity.resources.getString(R.string.cd_add_package)
        )

        composeTestRule.waitUntilExists(
            hasContentDescription(
                composeTestRule.activity.resources.getString(
                    R.string.cd_package_weight
                )
            )
        )

        addValueToEditable(
            composeTestRule.activity.resources.getString(
                R.string.cd_package_weight
            ), weight
        )

        addValueToEditable(
            composeTestRule.activity.resources.getString(
                R.string.cd_delivery_distance
            ), deliveryDistance
        )

        addValueToEditable(
            composeTestRule.activity.resources.getString(
                R.string.cd_offer_code
            ), offerCode
        )

        Espresso.closeSoftKeyboard()

        clickButton(
            composeTestRule.activity.resources.getString(R.string.cd_confirm_button)
        )

        composeTestRule.waitUntilExists(
            hasContentDescription(
                composeTestRule.activity.resources.getString(
                    R.string.cd_package_row, 1
                )
            )
        )
    }

    // Starts from one
    private fun removePackage(index: Int) {
        swipeToRemove(
            composeTestRule.activity.resources.getString(
                R.string.cd_package_row, index
            )
        )
    }

    private fun addValueToEditable(contentDesc: String, value: String) {
        composeTestRule
            .onNodeWithContentDescription(
                contentDesc
            )
            .performTextReplacement(value)

        composeTestRule
            .onNodeWithContentDescription(
                contentDesc
            )
            .fetchSemanticsNode()
            .assertEditableValue(value)
    }

    private fun swipeToRemove(contentDesc: String) {
        composeTestRule.waitUntilExists(
            hasContentDescription(
                contentDesc
            )
        )

        composeTestRule.onNodeWithContentDescription(
            contentDesc
        ).assertIsDisplayed()
            .performTouchInput {
                this.swipeLeft()
            }

        composeTestRule.onNodeWithContentDescription(
            contentDesc
        ).assertDoesNotExist()
    }

    private fun clickButton(contentDesc: String) {
        // scroll to button to make sure its visible
        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(
                    R.string.cd_scroll_container
                )
            )
            .tryScrollToNode(hasContentDescription(contentDesc))

        composeTestRule
            .onNodeWithContentDescription(
                contentDesc
            )
            .performClick()
    }
}