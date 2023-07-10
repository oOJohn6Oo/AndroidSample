package io.john6.sample.fragmentcommunicate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import io.john6.johnbase.compose.JohnAppTheme
import io.john6.johnbase.compose.spaceLarge
import io.john6.johnbase.compose.spaceMedium

/**
 * Common UI for all page in this module
 *
 * @param value the value to show in the [Text]
 * @param showSlider whether to show the [Slider]
 * @param backgroundColor background color
 * @param iconOfShowBtn the icon of [IconButton]
 * @param onInfoBtnClick the callback of [IconButton] with [Icons.Default.Info]
 * @param onShowBtnClick the callback of [IconButton] with [iconOfShowBtn]
 */
@Composable
fun CommonScreen(
    value: Int,
    showSlider: Boolean = false,
    backgroundColor: Color = Color.Transparent,
    iconOfShowBtn: ImageVector = Icons.Default.ImageSearch,
    onInfoBtnClick: () -> Unit,
    onShowBtnClick: (Int) -> Unit,
) {
    JohnAppTheme{
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.onSurface){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(horizontal = MaterialTheme.spaceMedium, vertical = MaterialTheme.spaceLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                var currentValue by remember(value) {
                    mutableStateOf(value.toFloat())
                }
                Text(text = "Value: $currentValue")
                if (showSlider){
                    Slider(value = currentValue, onValueChange = {
                        currentValue = it
                    }, valueRange = 0f..1314f)
                }
                Row(horizontalArrangement = Arrangement.Center) {
                    IconButton(onClick = onInfoBtnClick) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Info")
                    }
                    IconButton(onClick = { onShowBtnClick(currentValue.toInt()) }) {
                        Icon(
                            imageVector = iconOfShowBtn,
                            contentDescription = "Show Dialog"
                        )
                    }
                }
            }
        }

    }
}