import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import diary.Diary
import diary.DiaryEntry
import status.StatusEntry
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(
    diaries: List<Diary>,
    statuses: List<StatusEntry>,
    onSelectDiary: (Diary) -> Unit = {},
    onSelectEntry: (Diary, DiaryEntry) -> Unit = { _, _ -> },
    onCreateDiaryOpen: () -> Unit = {},
    onEditStatusOpen: (StatusEntry) -> Unit = {},
    onCreateStatusOpen: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val checkValues = remember { CheckManager(
        CheckValue("diaries", true),
        CheckValue("all entries", false),
        CheckValue("statuses", false)
    ) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(0.8F)
        ) {
            AppearDisappearAnimation(
                checkValues.getChecked() == "diaries" || checkValues.getChecked() == "statuses",
                modifier = Modifier.size(iconSize),
            ) {
                ButtonWithIcon(
                    icon = Icons.Default.Add,
                    onClick = {
                        when (checkValues.getChecked()) {
                            "diaries" -> onCreateDiaryOpen()
                            "statuses" -> onCreateStatusOpen()
                            else -> onCreateDiaryOpen()
                        }
                    },
                    modifier = Modifier.size(iconSize)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(padding, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .align(Alignment.Center),
            ) {
                checkValues.getKeys().forEach {
                    CheckButton(
                        text = it,
                        onClick = { checkValues.check(it) },
                        checked = checkValues.get(it) == true,
                    )
                }
            }
        }

        when(checkValues.getChecked()) {
            "diaries" -> DiariesList(
                diaries,
                onSelectDiary,
                modifier = Modifier.fillMaxWidth(0.8F),
            )
            "all entries" -> EntriesList(
                diaries.flatMap { it.entries },
                onSelect = { entry ->
                    val diary = diaries.find { it.entries.contains(entry) }
                    diary?.let { onSelectEntry(it, entry) }
                },
                modifier = Modifier.fillMaxWidth(0.8F),
            )
            "statuses" -> StatusesList(
                statuses,
                onEditStatusOpen = onEditStatusOpen,
                modifier = Modifier.fillMaxWidth(0.8F),
            )
            else -> {}
        }
    }
}

@Composable
private fun CheckButton(
    text: String,
    onClick: () -> Unit,
    checked: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = if (checked) colorTextSecond else colorText,
        style = TextStyle(fontSize = normalText, fontWeight = FontWeight.Bold),
        maxLines = 1,
        modifier = modifier
            .background(
                color = if (checked) colorBackgroundSecond else colorBackgroundSecondLighter,
                shape = RoundedCornerShape(smallCorners)
            )
            .padding(padding)
            .clickable(onClick = onClick, enabled = !checked)
    )
}

@Composable
private fun DiariesList(
    diaries: List<Diary>,
    onSelect: (Diary) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(padding, Alignment.CenterVertically),
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        diaries.forEach { diary ->
            DiaryCard(diary, onSelect)
        }
    }
}

@Composable
private fun DiaryCard(
    diary: Diary,
    onSelect: (Diary) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(colorBackground, RoundedCornerShape(corners))
            .padding(padding)
            .fillMaxWidth()
            .clickable { onSelect(diary) },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(padding),
        ) {
            Text(
                text = diary.title,
                color = colorText,
                style = TextStyle(fontSize = bigText, fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(padding).weight(1F),
            )

            Text(
                text = diary.entries.size.toString(),
                color = colorText,
                style = TextStyle(fontSize = smallText),
                maxLines = 1,
                modifier = Modifier.padding(padding),
            )
        }

        Text(
            text = diary.description,
            color = colorText,
            style = TextStyle(fontSize = normalText),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun StatusesList(
    statuses: List<StatusEntry>,
    onEditStatusOpen: (StatusEntry) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(padding, Alignment.CenterVertically),
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        statuses.sortedByDescending { it.dateCreate }.forEach { status ->
            StatusCard(status, onEditStatusOpen)
        }
    }
}

@Composable
private fun StatusCard(
    status: StatusEntry,
    onEditStatusOpen: (StatusEntry) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(colorBackground, RoundedCornerShape(corners))
            .padding(padding)
            .fillMaxWidth()
            .clickable { onEditStatusOpen(status) },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(padding),
        ) {
            val dateCreate = LocalDateTime.ofInstant(Instant.ofEpochMilli(status.dateCreate), ZoneId.systemDefault())
            val dateEdit = LocalDateTime.ofInstant(Instant.ofEpochMilli(status.dateEdit), ZoneId.systemDefault())

            val formatter = DateTimeFormatter.ofPattern("hh:mm a dd/MM/yyyy")
            val formattedDateCreate = formatter.format(dateCreate)
            val formattedDateEdit = formatter.format(dateEdit)

            Text(
                text = formattedDateCreate,
                color = colorText,
                style = TextStyle(fontSize = bigText, fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(padding),
            )

            if (status.dateCreate != status.dateEdit) {
                Text(
                    text = "(edited: $formattedDateEdit)",
                    color = colorText,
                    style = TextStyle(fontSize = normalText, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(padding),
                )
            }
        }

        Text(
            text = status.content,
            color = colorText,
            style = TextStyle(fontSize = normalText),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(padding),
        )
    }
}