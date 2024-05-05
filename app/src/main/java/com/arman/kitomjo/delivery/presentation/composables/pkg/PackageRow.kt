package com.arman.kitomjo.delivery.presentation.composables.pkg

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
import com.arman.kitomjo.delivery.presentation.entity.UIPackageEntity

@Composable
internal fun PackageRowContent(
    modifier: Modifier = Modifier,
    pkg: UIPackageEntity,
    contentDesc: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
            .semantics {
                contentDescription = contentDesc
            }
    ) {
        val pkgId = stringResource(id = R.string.package_id)
        Text(text = "$pkgId : ${pkg.id}")
        val maxSpeed = stringResource(id = R.string.package_weight)
        Text(text = "$maxSpeed : ${pkg.weight}")
        val maxLoad = stringResource(id = R.string.delivery_distance)
        Text(text = "$maxLoad : ${pkg.deliveryDistance}")

        if (!pkg.offerCode.isNullOrEmpty()) {
            val offerCode = stringResource(id = R.string.offer_code)
            Text(text = "$offerCode : ${pkg.offerCode}")
        }

        val deliveryPrice = stringResource(id = R.string.delivery_price)
        Text(text = "$deliveryPrice : ${pkg.price}")

        val deliveryTime = stringResource(id = R.string.delivery_time)
        Text(text = "$deliveryTime : ${pkg.deliveryTime}")
    }
}

@Preview(showBackground = true)
@Composable
private fun PackageRowContentPreview() {
    val pkgEntity = UIPackageEntity(
        "1",
        100.0,
        50.0,
        "offerCode",
        0.5,
        0.5
    )
    PackageRowContent(pkg = pkgEntity, contentDesc = "contentDesc")
}
