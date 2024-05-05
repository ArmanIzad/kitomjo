package com.arman.kitomjo.delivery.presentation.composables.vehicle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arman.kitomjo.R
import com.arman.kitomjo.delivery.presentation.DeliveryScreenEvent
import com.arman.kitomjo.delivery.presentation.composables.DefaultButton
import com.arman.kitomjo.delivery.presentation.entity.UITextProperty
import com.arman.kitomjo.delivery.presentation.entity.UIVehicleEntity
import com.arman.kitomjo.util.matchesDouble

@Composable
internal fun AddVehicleForm(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onEvent: (DeliveryScreenEvent) -> Unit
) {
    Column(
        modifier
            .wrapContentSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        var vehicleMaxSpeed by remember { mutableStateOf("") }
        var vehicleWeightLimit by remember { mutableStateOf("") }
        val maxSpeedCd = stringResource(id = R.string.cd_vehicle_max_speed)
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = maxSpeedCd
                },
            value = vehicleMaxSpeed,
            onValueChange = {
                if (it.isEmpty() || it.matchesDouble()) {
                    vehicleMaxSpeed = it
                }
            },
            label = { Text(stringResource(id = R.string.vehicle_max_speed)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
        )

        val maxLoadCd = stringResource(id = R.string.cd_vehicle_max_load)
        OutlinedTextField(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .semantics {
                    contentDescription = maxLoadCd
                },
            value = vehicleWeightLimit,
            onValueChange = {
                if (it.isEmpty() || it.matchesDouble()) {
                    vehicleWeightLimit = it
                }
            },
            label = { Text(stringResource(id = R.string.vehicle_max_load)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
        )

        DefaultButton(
            textProperty = UITextProperty(
                R.string.add_vehicle,
                R.string.cd_confirm_button
            ), isEnabled = vehicleMaxSpeed.isNotBlank() && vehicleWeightLimit.isNotBlank()
        ) {
            onEvent.invoke(
                DeliveryScreenEvent.AddVehicle(
                    UIVehicleEntity(
                        maxSpeed = vehicleMaxSpeed.toDouble(),
                        maxLoad = vehicleWeightLimit.toDouble(),
                    )
                )
            )
            onDismiss.invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddVehicleFormPreview() {
    AddVehicleForm(onDismiss = {}) {}
}