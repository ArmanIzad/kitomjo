package com.arman.kitomjo.delivery.presentation.composables.vehicle

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
import com.arman.kitomjo.delivery.presentation.entity.UIVehicleEntity

@Composable
internal fun VehicleRowContent(
    modifier: Modifier = Modifier,
    vehicle: UIVehicleEntity,
    contentDesc: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .background(Color.White)
            .semantics {
                contentDescription = contentDesc
            }
    ) {
        val vehicleId = stringResource(id = R.string.vehicle_id)
        Text(text = "$vehicleId : ${vehicle.id}")
        val maxSpeed = stringResource(id = R.string.vehicle_max_speed)
        Text(text = "$maxSpeed : ${vehicle.maxSpeed}")
        val maxLoad = stringResource(id = R.string.vehicle_max_load)
        Text(text = "$maxLoad : ${vehicle.maxLoad}")
    }
}

@Preview(showBackground = true)
@Composable
private fun VehicleRowContentPreview() {
    val vehicle = UIVehicleEntity("1", 100.0, 50.0)
    VehicleRowContent(vehicle = vehicle, contentDesc = "contentDesc")
}