import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun EditStatusWindow(
    previousContent: String = "",
    onDone: (String) -> Unit = {},
    onCancel: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var content by remember { mutableStateOf(previousContent) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(padding),
        modifier = modifier,
    ) {
        TextFieldWithText(
            text = content,
            name = "Content",
            onValueChange = { content = it },
            textFieldModifier = Modifier
                .padding(bottom = padding)
                .fillMaxWidth(0.9F),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(padding),
        ) {
            ButtonText(
                text = "Done",
                onClick = { onDone(content) },
            )

            ButtonText(
                text = "Cancel",
                onClick = onCancel,
            )
        }
    }
}