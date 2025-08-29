package org.fossify.messages.activities

import android.app.Application // For preview
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.fossify.messages.viewmodels.NotificationRulesViewModel
import org.fossify.messages.viewmodels.NotificationRulesViewModelFactory

class NotificationRulesActivity : ComponentActivity() {

    private val viewModel: NotificationRulesViewModel by viewModels {
        NotificationRulesViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                NotificationRulesScreen(viewModel)
            }
        }
    }
}

@Composable
fun NotificationRulesScreen(viewModel: NotificationRulesViewModel) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Rules", "Channels")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> RulesTabContent(viewModel)
            1 -> ChannelsTabContent(viewModel)
        }
    }
}

@Composable
fun RulesTabContent(viewModel: NotificationRulesViewModel) {
    var regexInput by remember { mutableStateOf("") }
    // Assuming categoryId 1L for now, this should be selectable or managed elsewhere
    val categoryId = 1L
    val rules by viewModel.rules.collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Add New Phone Number Regex Rule")

        OutlinedTextField(
            value = regexInput,
            onValueChange = { regexInput = it },
            label = { Text("Enter Regex") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (regexInput.isNotBlank()) {
                viewModel.addPhoneNumberRegexRule(regexInput, categoryId)
                regexInput = "" // Clear input field
            }
        }) {
            Text("Add Rule")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Current Rules (Count: ${rules.size})")
        rules.forEach {
            Text("ID: ${it.id}, Type: ${it.ruleType}, Value: ${it.valueToMatch}, Category: ${it.categoryId}, Order: ${it.order}")
        }
    }
}

@Composable
fun ChannelsTabContent(viewModel: NotificationRulesViewModel) {
    var channelNameInput by remember { mutableStateOf("") }
    val categories by viewModel.categories.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Add New Notification Channel")

        OutlinedTextField(
            value = channelNameInput,
            onValueChange = { channelNameInput = it },
            label = { Text("Enter Channel Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (channelNameInput.isNotBlank()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        viewModel.addNotificationCategory(channelNameInput, null, context)
                        channelNameInput = "" // Clear input field
                    } else {
                        // Handle cases for older Android versions if necessary
                        // (though minSdk is 26, this branch might not be strictly needed for channel creation)
                    }
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add Channel")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Registered Channels (Count: ${categories.size})")
        LazyColumn {
            items(categories) { category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = category.name, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                putExtra(Settings.EXTRA_CHANNEL_ID, category.channelId)
                            }
                            context.startActivity(intent)
                        }
                    }) {
                        Text("Settings")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            viewModel.deleteNotificationCategory(category, context)
                        }
                    }) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationRulesScreenPreview() {
    val application = Application() 
    val factory = NotificationRulesViewModelFactory(application)
    val fakeViewModel = factory.create(NotificationRulesViewModel::class.java)
    NotificationRulesScreen(fakeViewModel)
}

@Preview(showBackground = true)
@Composable
private fun RulesTabContentPreview() {
    val application = Application()
    val factory = NotificationRulesViewModelFactory(application)
    val fakeViewModel = factory.create(NotificationRulesViewModel::class.java)
    RulesTabContent(fakeViewModel)
}

@Preview(showBackground = true)
@Composable
private fun ChannelsTabContentPreview() {
    val application = Application()
    val factory = NotificationRulesViewModelFactory(application)
    val fakeViewModel = factory.create(NotificationRulesViewModel::class.java)
    ChannelsTabContent(fakeViewModel)
}
