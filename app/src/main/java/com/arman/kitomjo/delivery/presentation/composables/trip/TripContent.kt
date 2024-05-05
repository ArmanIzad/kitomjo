package com.arman.kitomjo.delivery.presentation.composables.trip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arman.kitomjo.R
import com.arman.kitomjo.delivery.domain.entity.AppException
import com.arman.kitomjo.delivery.domain.entity.PackageToDeliver
import com.arman.kitomjo.delivery.domain.entity.Trip
import com.arman.kitomjo.delivery.presentation.DeliveryScreenEvent
import com.arman.kitomjo.delivery.presentation.composables.DefaultButton
import com.arman.kitomjo.delivery.presentation.entity.UIPackageEntity
import com.arman.kitomjo.delivery.presentation.entity.UITextProperty
import com.arman.kitomjo.delivery.presentation.entity.UIVehicleEntity

@Composable
internal fun TripsContent(
    modifier: Modifier = Modifier,
    vehicles: List<UIVehicleEntity>,
    packages: List<UIPackageEntity>,
    trips: List<Trip>,
    exception: AppException?,
    onEvent: (DeliveryScreenEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val isEnabled =
            vehicles.isNotEmpty() && packages.isNotEmpty() && (exception == null || exception !is AppException.InvalidBaseDeliveryCost)
        DefaultButton(
            textProperty = UITextProperty(
                R.string.calculate_trips,
                R.string.cd_calculate_trips
            ), isEnabled = isEnabled
        ) {
            onEvent.invoke(DeliveryScreenEvent.CalculateTrips)
        }

        if (exception != null && exception is AppException.NoAvailableVehicles) {
            val contentDesc = stringResource(R.string.cd_trips_error)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = contentDesc
                    },
                text = stringResource(exception.errorMessageResId),
                color = MaterialTheme.colorScheme.error
            )
        } else {
            trips.forEachIndexed { index, trip ->
                TripRow(trip = trip, index = index)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TripsContentPreview() {
    val vehicles = listOf(UIVehicleEntity("1", 12.0, 12.0), UIVehicleEntity("2", 20.0, 20.0))
    val packages = listOf(
        UIPackageEntity(
            "1",
            100.0,
            50.0,
            "offerCode",
            0.5,
            0.5
        ), UIPackageEntity(
            "2",
            100.0,
            50.0,
            "offerCode",
            0.5,
            0.5
        )
    )
    val trips = listOf(
        Trip(
            "1",
            listOf(
                PackageToDeliver("4", 110.0, 60.0, null, 0.85),
                PackageToDeliver("2", 75.0, 125.0, null, 1.78)
            )
        ),
        Trip("2", listOf(PackageToDeliver("3", 175.0, 100.0, null, 1.42))),
        Trip("2", listOf(PackageToDeliver("5", 155.0, 95.0, null, 4.19))),
        Trip("1", listOf(PackageToDeliver("1", 50.0, 30.0, null, 3.98)))
    )
    TripsContent(
        vehicles = vehicles,
        packages = packages,
        trips = trips,
        exception = null
    ) { }
}
