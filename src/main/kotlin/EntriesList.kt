import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import diary.DiaryEntry
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun EntriesList(
    entries: List<DiaryEntry>,
    onSelect: (DiaryEntry) -> Unit = {},
    onBackButton: Boolean = false,
    onBack: () -> Unit = {},
    onCreateEntryButton: Boolean = false,
    onCreateEntry: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(padding),
        modifier = modifier,
    ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(padding),
            ) {
                if (onBackButton) {
                    ButtonWithIcon(
                        icon = Icons.Default.ArrowBack,
                        onClick = onBack,
                        modifier = Modifier.size(iconSize)
                    )
                }

                if (onCreateEntryButton) {
                    ButtonWithIcon(
                        icon = Icons.Default.Add,
                        onClick = onCreateEntry,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }

        Column(
            verticalArrangement = Arrangement.spacedBy(padding),
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            entries.sortedByDescending { it.dateCreate }.forEach { entry ->
                EntryCard(entry, onSelect)
            }
        }
    }
}

@Composable
private fun EntryCard(
    entry: DiaryEntry,
    onSelect: (DiaryEntry) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .background(colorBackground, RoundedCornerShape(corners))
            .padding(padding)
            .fillMaxWidth()
            .clickable { onSelect(entry) },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(padding),
        ) {
            Text(
                text = entry.title,
                color = colorText,
                style = TextStyle(fontSize = bigText, fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(padding)
                    .weight(1F),
            )

            val dateCreate = LocalDateTime.ofInstant(Instant.ofEpochMilli(entry.dateCreate), ZoneId.systemDefault())
            val dateEdit = LocalDateTime.ofInstant(Instant.ofEpochMilli(entry.dateEdit), ZoneId.systemDefault())

            val formatter = DateTimeFormatter.ofPattern("hh:mm a dd/MM/yyyy")
            val formattedDateCreate = formatter.format(dateCreate)
            val formattedDateEdit = formatter.format(dateEdit)

            Text(
                text = formattedDateCreate,
                color = colorText,
                style = TextStyle(fontSize = smallText),
                maxLines = 1,
                modifier = Modifier.padding(padding),
            )

            if (entry.dateCreate != entry.dateEdit) {
                Text(
                    text = "(edited: $formattedDateEdit)",
                    color = colorText,
                    style = TextStyle(fontSize = smallText, fontStyle = FontStyle.Italic),
                    maxLines = 1,
                    modifier = Modifier.padding(padding),
                )
            }
        }

        Text(
            text = "${entry.content}\n\n\n",
            color = colorText,
            style = TextStyle(fontSize = normalText),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(padding),
        )
    }
}