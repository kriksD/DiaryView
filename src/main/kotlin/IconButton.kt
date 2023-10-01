import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ButtonWithIcon(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = colorText,
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Composable
fun ButtonWithIcon(
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = icon,
        contentDescription = null,
        tint = colorText,
        modifier = modifier.clickable(onClick = onClick)
    )
}