package org.fossify.messages.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import org.fossify.messages.viewmodels.NotificationRulesViewModel
import org.fossify.messages.viewmodels.NotificationRulesViewModelFactory

class NotificationRulesActivity : ComponentActivity() {

    private val viewModel: NotificationRulesViewModel by viewModels {
        NotificationRulesViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotificationRulesScreen(viewModel)
        }
    }
}

@Composable
fun NotificationRulesScreen(viewModel: NotificationRulesViewModel? = null) {
    // TODO: Implement the UI for managing notification rules and categories
    if (viewModel != null) {
        val rules by viewModel.rules.collectAsState()
        Text("Notification Rules (Count: ${rules.size}) and Categories will be here!")
    } else {
        Text("Notification Rules and Categories will be here! (ViewModel not available for preview)")
    }
}

@Preview
@Composable
private fun NotificationRulesPreview() {
    NotificationRulesScreen()
}
