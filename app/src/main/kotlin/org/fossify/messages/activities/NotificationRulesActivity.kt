package org.fossify.messages.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
fun NotificationRulesScreen(viewModel: NotificationRulesViewModel? = null) {
    var regexInput by remember { mutableStateOf("") }
    val rules by viewModel?.rules?.collectAsState(initial = emptyList()) ?: remember { mutableStateOf(emptyList()) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Add New Phone Number Regex Rule")

        OutlinedTextField(
            value = regexInput,
            onValueChange = { regexInput = it },
            label = { Text("Enter Regex") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (regexInput.isNotBlank() && viewModel != null) {
                // Using a placeholder categoryId for now
                viewModel.addPhoneNumberRegexRule(regexInput, 1L) 
                regexInput = "" // Clear input field
            }
        }) {
            Text("Add Rule")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Current Rules (Count: ${rules.size})")
        // TODO: Display the list of rules here
        rules.forEach {
            Text("ID: ${it.id}, Type: ${it.ruleType}, Value: ${it.valueToMatch}, Category: ${it.categoryId}, Order: ${it.order}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationRulesPreview() {
    NotificationRulesScreen()
}
