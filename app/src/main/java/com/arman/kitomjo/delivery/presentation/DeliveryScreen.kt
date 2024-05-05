package com.arman.kitomjo.delivery.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arman.kitomjo.R
import com.arman.kitomjo.delivery.domain.entity.PackageToDeliver
import com.arman.kitomjo.delivery.domain.entity.Trip
import com.arman.kitomjo.delivery.presentation.composables.BaseDeliveryCostContent
import com.arman.kitomjo.delivery.presentation.composables.pkg.PackageContent
import com.arman.kitomjo.delivery.presentation.composables.trip.TripsContent
import com.arman.kitomjo.delivery.presentation.composables.vehicle.VehicleContent
import com.arman.kitomjo.delivery.presentation.entity.UIPackageEntity
import com.arman.kitomjo.delivery.presentation.entity.UIVehicleEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryScreen(modifier: Modifier = Modifier, viewModel: DeliveryViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                }
            )
        }
    ) { innerPadding ->
        DeliveryContent(modifier.padding(innerPadding), state, viewModel::onEvent)
    }
}

@Composable
fun DeliveryContent(
    modifier: Modifier = Modifier,
    state: State<DeliveryScreenState>,
    onEvent: (DeliveryScreenEvent) -> Unit
) {
    val packages = state.value.packages
    val vehicles = state.value.vehicles
    val trips = state.value.trips
    val baseDeliveryCost = state.value.deliveryBaseCost
    val exception = state.value.exception
    val contentDesc = stringResource(id = R.string.cd_scroll_container)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .semantics {
                contentDescription = contentDesc
            }
    ) {

        item {
            BaseDeliveryCostContent(baseDeliveryCost = baseDeliveryCost, exception = exception) {
                onEvent(it)
            }
        }

        item {
            VehicleContent(vehicles = vehicles, onEvent = onEvent)
        }

        item {
            PackageContent(packages = packages, onEvent = onEvent)
        }

        item {
            TripsContent(
                vehicles = vehicles,
                packages = packages,
                trips = trips,
                exception = exception,
                onEvent = onEvent
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun DeliveryContentPreview() {
    val deliveryScreenState = DeliveryScreenState(
        vehicles = listOf(
            UIVehicleEntity("1", 12.0, 12.0),
            UIVehicleEntity("2", 20.0, 20.0)
        ),
        packages = listOf(
            UIPackageEntity(
                "1",
                100.0,
                50.0,
                "offerCode",
                0.5,
                0.5
            ),
            UIPackageEntity(
                "2",
                100.0,
                50.0,
                "offerCode",
                0.5,
                0.5
            )
        ),
        trips = listOf(
            Trip(
                "1",
                listOf(
                    PackageToDeliver("4", 110.0, 60.0, null, 0.85),
                    PackageToDeliver("2", 75.0, 125.0, null, 1.78)
                )
            )
        ),
        deliveryBaseCost = 100.0,
        exception = null
    )
    val state = remember { mutableStateOf(deliveryScreenState) }
    DeliveryContent(state = state) {}
}
