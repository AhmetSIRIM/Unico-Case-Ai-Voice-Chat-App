package com.ahmetsirim.unicocaseaivoicechatapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ahmetsirim.designsystem.theme.UnicoCaseAiVoiceChatAppTheme
import com.ahmetsirim.navigation.UnicoCaseAiVoiceChatNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnicoCaseAiVoiceChatAppTheme {
                UnicoCaseAiVoiceChatNavHost()
            }
        }
    }
}
