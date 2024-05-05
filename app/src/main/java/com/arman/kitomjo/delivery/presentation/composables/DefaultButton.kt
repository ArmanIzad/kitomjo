package com.arman.kitomjo.delivery.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arman.kitomjo.R
import com.arman.kitomjo.delivery.presentation.entity.UITextProperty

@Composable
internal fun DefaultButton(
    modifier: Modifier = Modifier,
    textProperty: UITextProperty,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    val cd = stringResource(id = textProperty.cdResId)
    Button(modifier = modifier
        .fillMaxWidth()
        .height(60.dp)
        .semantics {
            contentDescription = cd
        }, onClick = {
        onClick.invoke()
    }, enabled = isEnabled
    ) {
        Text(stringResource(id = textProperty.textResId))
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultButtonPreview() {
    val textProperty = UITextProperty(R.string.cd_add_package, R.string.add_package)
    DefaultButton(textProperty = textProperty) {}
}