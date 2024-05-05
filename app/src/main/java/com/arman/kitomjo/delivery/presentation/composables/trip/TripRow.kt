package com.arman.kitomjo.delivery.presentation.composables.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arman.kitomjo.R
import com.arman.kitomjo.delivery.domain.entity.PackageToDeliver
import com.arman.kitomjo.delivery.domain.entity.Trip
import com.arman.kitomjo.delivery.presentation.composables.pkg.PackageRowContent
import com.arman.kitomjo.delivery.presentation.entity.toUIEntity

@Composable
internal fun TripRow(modifier: Modifier = Modifier, trip: Trip, index: Int) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
    ) {
        val vehicleCd = stringResource(id = R.string.cd_vehicle_row, index + 1)
        val vehicleId = stringResource(id = R.string.vehicle_id)
        Text(modifier = Modifier.semantics {
            contentDescription = vehicleCd
        }, text = "$vehicleId : ${trip.vehicleId}")
        trip.packages.forEachIndexed { index, pkg ->
            val tripCd = stringResource(id = R.string.cd_trips_row, index + 1)
            PackageRowContent(
                modifier = Modifier.padding(horizontal = 8.dp),
                pkg = pkg.toUIEntity(),
                tripCd
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TripRowPreview() {
    val trip = Trip(
        "1",
        listOf(
            PackageToDeliver("4", 110.0, 60.0, null, 0.85),
            PackageToDeliver("2", 75.0, 125.0, null, 1.78)
        )
    )
    TripRow(trip = trip, index = 0)
}