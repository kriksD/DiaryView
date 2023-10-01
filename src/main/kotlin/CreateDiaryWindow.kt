import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CreateDiaryWindow(
    onCreate: (String, String) -> Unit = { _, _ -> },
    onCancel: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(padding),
        modifier = modifier,
    ) {
        TextFieldWithText(
            text = title,
            name = "Title",
            showCharCount = false,
            singleLine = true,
            onValueChange = { title = it },
            modifier = Modifier
                .fillMaxWidth(0.9F),
        )

        TextFieldWithText(
            text = description,
            name = "Description",
            onValueChange = { description = it },
            textFieldModifier = Modifier
                .padding(bottom = padding)
                .fillMaxWidth(0.9F),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(padding),
        ) {
            ButtonText(
                text = "Create",
                onClick = { onCreate(title, description) },
            )

            ButtonText(
                text = "Cancel",
                onClick = onCancel,
            )
        }
    }
}