package org.fossify.messages.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class NotificationRulesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotificationRulesScreen()
        }
    }
}

@Composable
fun NotificationRulesScreen() {
    // TODO: Implement the UI for managing notification rules and categories
    Text("Notification Rules and Categories will be here!")
}
