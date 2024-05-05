package com.arman.kitomjo.delivery.presentation.composables.pkg

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
import com.arman.kitomjo.delivery.presentation.entity.UIPackageEntity
import com.arman.kitomjo.delivery.presentation.entity.UITextProperty
import com.arman.kitomjo.util.matchesDouble

@Composable
internal fun AddPackageForm(
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
        var weight by remember { mutableStateOf("") }
        var deliveryDistance by remember { mutableStateOf("") }
        var offerCode by remember { mutableStateOf("") }
        val pkgWeightCd = stringResource(id = R.string.cd_package_weight)

        OutlinedTextField(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .semantics {
                    contentDescription = pkgWeightCd
                },
            value = weight,
            onValueChange = {
                if (it.isEmpty() || it.matchesDouble()) {
                    weight = it
                }
            },
            label = { Text(stringResource(id = R.string.package_weight)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
        )

        val deliveryDistanceCd = stringResource(id = R.string.cd_delivery_distance)
        OutlinedTextField(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .semantics {
                    contentDescription = deliveryDistanceCd
                },
            value = deliveryDistance,
            onValueChange = {
                if (it.isEmpty() || it.matchesDouble()) {
                    deliveryDistance = it
                }
            },
            label = { Text(stringResource(id = R.string.delivery_distance)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
        )

        val offerCodeCd = stringResource(id = R.string.cd_offer_code)
        OutlinedTextField(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .semantics {
                    contentDescription = offerCodeCd
                },
            value = offerCode,
            onValueChange = {
                offerCode = it
            },
            label = { Text(stringResource(id = R.string.offer_code)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
        )

        DefaultButton(
            textProperty = UITextProperty(
                R.string.add_package,
                R.string.cd_confirm_button
            ), isEnabled = weight.isNotBlank() && deliveryDistance.isNotBlank()
        ) {
            onEvent.invoke(
                DeliveryScreenEvent.AddPackage(
                    UIPackageEntity(
                        weight = weight.toDouble(),
                        deliveryDistance = deliveryDistance.toDouble(),
                        offerCode = offerCode
                    )
                )
            )
            onDismiss.invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddPackageFormPreview() {
    AddPackageForm(onDismiss = {}) {}
}