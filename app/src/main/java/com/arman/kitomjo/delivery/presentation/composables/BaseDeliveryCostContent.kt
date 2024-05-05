package com.arman.kitomjo.delivery.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arman.kitomjo.R
import com.arman.kitomjo.delivery.domain.entity.AppException
import com.arman.kitomjo.delivery.presentation.DeliveryScreenEvent
import com.arman.kitomjo.util.processInput

@Composable
internal fun BaseDeliveryCostContent(
    modifier: Modifier = Modifier,
    baseDeliveryCost: Double?,
    exception: AppException?,
    onEvent: (DeliveryScreenEvent) -> Unit
) {
    var baseCost by rememberSaveable { mutableStateOf(baseDeliveryCost.toString()) }
    val hasError = exception != null && exception is AppException.InvalidBaseDeliveryCost
    val cd = stringResource(id = R.string.cd_base_delivery_cost)
    Row(modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp)) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .semantics {
                    contentDescription = cd
                },
            value = baseCost,
            onValueChange = {
                baseCost = it.processInput()
                onEvent.invoke(DeliveryScreenEvent.UpdateDeliveryBaseCost(baseCost))
            },
            label = { Text(stringResource(id = R.string.base_delivery_cost)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = hasError,
            supportingText = {
                if (hasError) {
                    val errorCd = stringResource(id = R.string.cd_base_delivery_cost_error)
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics {
                                contentDescription = errorCd
                            },
                        text = stringResource(exception?.errorMessageResId ?: -1),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BaseDeliveryCostContentPreview() {
    BaseDeliveryCostContent(baseDeliveryCost = 12.0, exception = null) {}
}