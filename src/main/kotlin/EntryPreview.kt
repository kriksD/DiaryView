import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import diary.DiaryEntry

@Composable
fun EntryPreview(
    entry: DiaryEntry,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var title by remember { mutableStateOf(entry.title) }
    var content by remember { mutableStateOf(entry.content) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(padding),
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(padding),
            modifier = Modifier.fillMaxWidth(0.9F)
        ) {
            ButtonWithIcon(
                icon = Icons.Default.ArrowBack,
                onClick = onBack,
                modifier = Modifier.size(iconSize)
            )

            TextFieldWithText(
                text = title,
                name = "Title",
                showCharCount = false,
                singleLine = true,
                onValueChange = {
                    title = it
                    entry.title = it
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        TextFieldWithText(
            text = content,
            name = "Content",
            onValueChange = {
                content = it
                entry.content = it
            },
            textFieldModifier = Modifier
                .weight(1F)
                .padding(bottom = padding)
                .fillMaxWidth(0.9F),
        )
    }
}

@Composable
fun TextFieldWithText(
    text: String,
    name: String = "",
    onValueChange: (String) -> Unit = {},
    singleLine: Boolean = false,
    showCharCount: Boolean = true,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(padding)
        ) {
            Text(name, fontSize = bigText, color = colorText)

            if (showCharCount) {
                Text(
                    "${text.length} Chars",
                    fontSize = smallText,
                    color = colorTextSecond
                )
            }
        }

        BasicTextField(
            modifier = textFieldModifier
                .fillMaxWidth()
                .background(colorBackground, RoundedCornerShape(corners))
                .border(smallBorder, colorBorder, RoundedCornerShape(corners))
                .padding(padding),
            value = text,
            textStyle = TextStyle(
                color = colorText,
                fontSize = normalText,
            ),
            cursorBrush = SolidColor(colorText),
            onValueChange = {
                onValueChange.invoke(it)
            },
            singleLine = singleLine,
            maxLines = if (singleLine) 1 else Int.MAX_VALUE,
        )
    }
}