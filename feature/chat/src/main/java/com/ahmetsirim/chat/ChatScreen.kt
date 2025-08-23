package com.ahmetsirim.chat

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmetsirim.common.utility.launchAppDetailsSettings
import com.ahmetsirim.designsystem.R
import com.ahmetsirim.designsystem.component.InformationalDialog
import com.ahmetsirim.designsystem.utility.ResponsivenessCheckerPreview
import com.ahmetsirim.domain.model.ChatMessage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatScreen(
    uiState: ChatContract.UiState,
    onEvent: (ChatContract.UiEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = LocalActivity.current

    val permissionRequesterActivityResultLauncher: ManagedActivityResultLauncher<String, Boolean> = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { permission ->
            when (permission) {
                true -> TODO()
                false -> onEvent(ChatContract.UiEvent.OnShowMicrophonePermissionRationale)
            }
        }
    )

    LaunchedEffect(Unit) {
        handleMicrophonePermissionRequest(
            context = context,
            onEvent = onEvent,
            permissionRequesterActivityResultLauncher = permissionRequesterActivityResultLauncher,
            activity = activity
        )
    }

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(uiState.messages.size - 1)
            }
        }
    }

    if (uiState.isRecordAudioPermissionRationaleInformationalDialogOpen) {
        InformationalDialog(
            description = "This permission is important for the application to function properly. Please grant permission.",
            buttonTextAndActionPair = Pair("Go to Settings") {
                context.launchAppDetailsSettings()
                onEvent(ChatContract.UiEvent.OnShowMicrophonePermissionRationale)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AI Assistant",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                },
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Canvas(
                    Modifier
                        .size(100.dp)
                ) {
                    drawCircle(
                        color = Color.Red,
                        radius = size.minDimension / 2
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            uiState.errorState?.let {
                InformationalDialog(
                    icon = R.drawable.ic_info_box_error,
                    description = stringResource(it.exceptionMessageResId),
                    buttonTextAndActionPair = Pair(
                        first = stringResource(it.exceptionSolutionSuggestionResId),
                        second = { onEvent(ChatContract.UiEvent.UserNotifiedTheError) }
                    )
                )
            }

            ChatContent(
                messages = uiState.messages,
                isAiTyping = uiState.isAiTyping,
                listState = listState,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ChatContent(
    modifier: Modifier = Modifier,
    messages: List<ChatMessage>,
    isAiTyping: Boolean,
    listState: LazyListState,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = messages,
        ) { message ->
            MessageBubble(message = message)
        }

        if (isAiTyping) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Card(
                        modifier = Modifier.padding(end = 48.dp),
                        shape = RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        ),
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Yazıyor...",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isFromUser) {
            // AI Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🤖",
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .let {
                    if (message.isFromUser) it.padding(start = 48.dp)
                    else it.padding(end = 48.dp)
                },
            shape = RoundedCornerShape(
                topStart = if (message.isFromUser) 16.dp else 4.dp,
                topEnd = if (message.isFromUser) 4.dp else 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isFromUser)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = if (message.isFromUser)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@ResponsivenessCheckerPreview
@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ChatScreenPreview() {
    val messages = listOf(
        ChatMessage(content = "Merhaba, nasılsın?", isFromUser = true),
        ChatMessage(content = "Harikayım, teşekkür ederim. Size nasıl yardımcı olabilirim?", isFromUser = false),
        ChatMessage(content = "Son çıkan filmler hakkında bilgi alabilir miyim?", isFromUser = true),
        ChatMessage(content = "Elbette. Hangi tür filmlerle ilgileniyorsunuz?", isFromUser = false),
        ChatMessage(content = "Bilim kurgu ve aksiyon filmleri ilgimi çekiyor.", isFromUser = true),
        ChatMessage(content = "Öyleyse, 'Dune: Part Two' ve 'Oppenheimer'ı izlemenizi tavsiye ederim.", isFromUser = false),
        ChatMessage(content = "Teşekkürler, not aldım.", isFromUser = true),
        ChatMessage(content = "Rica ederim. Başka bir konuda sorunuz var mı?", isFromUser = false),
        ChatMessage(content = "Hava durumu nasıl?", isFromUser = true),
        ChatMessage(content = "Lütfen konum bilginizi paylaşın veya bir şehir adı belirtin.", isFromUser = false),
        ChatMessage(content = "İstanbul için hava durumu nedir?", isFromUser = true),
        ChatMessage(content = "İstanbul'da hava şu an ılık ve parçalı bulutlu.", isFromUser = false),
        ChatMessage(content = "Çok teşekkür ederim.", isFromUser = true),
        ChatMessage(content = "Ne demek. Başka bir şey lazım olursa çekinmeyin.", isFromUser = false),
        ChatMessage(content = "Yapılacaklar listesi oluşturmama yardım eder misin?", isFromUser = true),
        ChatMessage(content = "Tabii, listeyi hazırlayabiliriz. Yapmak istediğiniz şeyleri söyleyin.", isFromUser = false),
        ChatMessage(content = "Alışverişe git, raporu bitir ve spora başla.", isFromUser = true),
        ChatMessage(content = "Liste oluşturuldu: 1. Alışverişe git. 2. Raporu bitir. 3. Spora başla.", isFromUser = false),
        ChatMessage(content = "Mükemmel, çok işime yarayacak.", isFromUser = true),
        ChatMessage(content = "Yardımcı olabildiğime sevindim.", isFromUser = false)
    )

    ChatScreen(
        uiState = ChatContract.UiState(messages = messages),
        onEvent = {}
    )
}