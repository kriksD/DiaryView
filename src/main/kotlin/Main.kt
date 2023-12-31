import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import diary.DiariesLoader
import diary.Diary
import diary.DiaryEntry
import properties.Properties
import status.StatusEntry
import status.StatusesLoader

fun main() = application {
    Properties.loadSettings()
    Properties.loadStyle()

    val diaries by remember { mutableStateOf(DiariesLoader()) }
    diaries.load()

    val statuses by remember { mutableStateOf(StatusesLoader()) }
    statuses.load()

    val screen by remember { mutableStateOf(CheckManager(
        CheckValue(Screen.Main, true),
        CheckValue(Screen.Diary, false),
        CheckValue(Screen.Entry, false),
    )) }
    var selectedDiary by remember { mutableStateOf<Diary?>(null) }
    var selectedEntry by remember { mutableStateOf<DiaryEntry?>(null) }

    var selectedStatus by remember { mutableStateOf<StatusEntry?>(null) }

    var isCreateDiaryWindowOpen by remember { mutableStateOf(false) }
    var isEditStatusWindowOpen by remember { mutableStateOf(false) }

    val windowState = rememberWindowState(
        width = 1400.dp,
        height = 700.dp,
        position = WindowPosition.Aligned(Alignment.Center)
    )

    Window(
        onCloseRequest = {
            diaries.save()
            exitApplication()
        },
        state = windowState
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(colorBackgroundLighter),
        ) {
            when(screen.getChecked()) {
                Screen.Main -> MainScreen(
                    diaries = diaries.diaries,
                    statuses = statuses.statuses,
                    onSelectDiary = {
                        selectedDiary = it
                        selectedEntry = null
                        screen.check(Screen.Diary)
                    },
                    onSelectEntry = { diary, entry ->
                        selectedDiary = diary
                        selectedEntry = entry
                        screen.check(Screen.Entry)
                    },
                    onCreateDiaryOpen = {
                        isCreateDiaryWindowOpen = true
                    },
                    onEditStatusOpen = { status ->
                        selectedStatus = status
                        isEditStatusWindowOpen = true
                    },
                    onCreateStatusOpen = {
                        isEditStatusWindowOpen = true
                    },
                    modifier = Modifier.fillMaxSize(),
                )
                Screen.Diary -> EntriesList(
                    selectedDiary?.entries ?: return@Window,
                    onSelect = {
                        selectedEntry = it
                        screen.check(Screen.Entry)
                    },
                    onBackButton = true,
                    onBack = {
                        selectedDiary = null
                        screen.check(Screen.Main)
                    },
                    onCreateEntryButton = true,
                    onCreateEntry = {
                        selectedEntry = selectedDiary?.createEntry("New entry in \"${selectedDiary?.title}\" diary")
                        screen.check(Screen.Entry)
                        diaries.save()
                    },
                    modifier = Modifier.fillMaxWidth(0.8F),
                )
                Screen.Entry -> EntryPreview(
                    selectedEntry ?: return@Window,
                    onBack = {
                        selectedEntry = null
                        screen.check(Screen.Diary)
                        diaries.save()
                    },
                    modifier = Modifier.fillMaxSize(),
                )
                else -> {}
            }

            AppearDisappearAnimation(
                isCreateDiaryWindowOpen,
                normalAnimationDuration,
                Modifier.align(Alignment.Center),
            ) {
                CreateDiaryWindow(
                    onCreate = { title, description ->
                        diaries.addDiary(title, description)
                        isCreateDiaryWindowOpen = false
                        diaries.save()
                    },
                    onCancel = { isCreateDiaryWindowOpen = false },
                    modifier = Modifier
                        .fillMaxWidth(0.65F)
                        .heightIn(0.dp, (window.height * 0.8F).dp)
                        .background(colorBackground.copy(transparencySecond), RoundedCornerShape(corners))
                        .border(smallBorder, colorBorder, RoundedCornerShape(corners))
                        .padding(padding),
                )
            }

            AppearDisappearAnimation(
                isEditStatusWindowOpen,
                normalAnimationDuration,
                Modifier.align(Alignment.Center),
            ) {
                EditStatusWindow(
                    previousContent = selectedStatus?.content ?: "",
                    onDone = { content ->
                        selectedStatus?.content = content
                        if (selectedStatus == null) statuses.addStatus(content)

                        selectedStatus = null
                        isEditStatusWindowOpen = false
                        statuses.save()
                    },
                    onCancel = {
                        selectedStatus = null
                        isEditStatusWindowOpen = false
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.65F)
                        .heightIn(0.dp, (window.height * 0.8F).dp)
                        .background(colorBackground.copy(transparencySecond), RoundedCornerShape(corners))
                        .border(smallBorder, colorBorder, RoundedCornerShape(corners))
                        .padding(padding),
                )
            }
        }
    }
}
