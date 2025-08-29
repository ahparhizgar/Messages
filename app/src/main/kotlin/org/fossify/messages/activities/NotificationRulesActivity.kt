package org.fossify.messages.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
            NotificationRulesScreen(viewModel)
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
    // Placeholder for Channels content
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Channels Management (Coming Soon)")
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationRulesScreenPreview() {
    // This preview won't have a real ViewModel, so state will be empty.
    // You might need a more sophisticated preview setup for complex ViewModel interactions.
    val fakeViewModel = NotificationRulesViewModel(null!!) // Or a mock/fake implementation
    NotificationRulesScreen(fakeViewModel)
}

@Preview(showBackground = true)
@Composable
private fun RulesTabContentPreview() {
    val fakeViewModel = NotificationRulesViewModel(null!!)
    RulesTabContent(fakeViewModel)
}

@Preview(showBackground = true)
@Composable
private fun ChannelsTabContentPreview() {
     val fakeViewModel = NotificationRulesViewModel(null!!)
    ChannelsTabContent(fakeViewModel)
}
