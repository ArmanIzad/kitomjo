package com.arman.kitomjo.delivery.presentation.composables.pkg

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
import com.arman.kitomjo.delivery.presentation.entity.UIPackageEntity
import com.arman.kitomjo.delivery.presentation.entity.UITextProperty
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PackageContent(
    modifier: Modifier = Modifier,
    packages: List<UIPackageEntity>,
    onEvent: (DeliveryScreenEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DefaultButton(
            textProperty = UITextProperty(
                R.string.add_package,
                R.string.cd_add_package
            )
        ) {
            showBottomSheet.value = true
        }

        packages.forEachIndexed { index, pkg ->
            val contentDesc = stringResource(id = R.string.cd_package_row, index + 1)
            SwipeToDismissContainer(item = pkg, onDelete = {
                onEvent.invoke(DeliveryScreenEvent.RemovePackage(pkg.id))
            }) {
                PackageRowContent(modifier, it, contentDesc)
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
            AddPackageForm(Modifier, {
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
private fun PackageContentPreview() {
    val pkg = UIPackageEntity("1", 10.0, 10.0, "offer", 10.0, 10.0, 10.0)
    PackageContent(packages = listOf(pkg)) {}
}