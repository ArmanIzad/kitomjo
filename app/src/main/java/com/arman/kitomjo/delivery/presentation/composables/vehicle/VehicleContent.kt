package com.arman.kitomjo.delivery.presentation.composables.vehicle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arman.kitomjo.R
import com.arman.kitomjo.delivery.presentation.DeliveryScreenEvent
import com.arman.kitomjo.delivery.presentation.composables.DefaultButton
import com.arman.kitomjo.delivery.presentation.composables.SwipeToDismissContainer
import com.arman.kitomjo.delivery.presentation.entity.UITextProperty
import com.arman.kitomjo.delivery.presentation.entity.UIVehicleEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun VehicleContent(
    modifier: Modifier = Modifier,
    vehicles: List<UIVehicleEntity>,
    onEvent: (DeliveryScreenEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        DefaultButton(
            textProperty = UITextProperty(
                R.string.add_vehicle,
                R.string.cd_add_vehicle
            )
        ) {
            showBottomSheet.value = true
        }

        vehicles.forEachIndexed { index, vehicle ->
            val contentDesc = stringResource(id = R.string.cd_vehicle_row, index + 1)
            SwipeToDismissContainer(item = vehicle, onDelete = {
                onEvent.invoke(DeliveryScreenEvent.RemoveVehicle(vehicle.id))
            }) {
                VehicleRowContent(modifier, it, contentDesc)
            }
        }
    }

    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet.value = false
            },
            sheetState = sheetState
        ) {
            AddVehicleForm(Modifier, {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet.value = false
                    }
                }
            }, onEvent = onEvent)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VehicleContentPreview() {
    val vehicle = UIVehicleEntity("1", 100.0, 50.0)
    VehicleContent(vehicles = listOf(vehicle)) {}
}
