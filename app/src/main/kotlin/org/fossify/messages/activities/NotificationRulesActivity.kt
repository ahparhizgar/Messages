package org.fossify.messages.activities

import android.app.Application // For preview
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.fossify.messages.viewmodels.NotificationRulesViewModel
import org.koin.androidx.compose.koinViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

class NotificationRulesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    // Use Koin to inject ViewModel
                    val viewModel: NotificationRulesViewModel = koinViewModel()
                    NotificationRulesScreen(viewModel)
                }
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
    val ruleTypes = listOf(
        "Exact Phone Number" to org.fossify.messages.models.NotificationRuleType.PHONE_NUMBER_EXACT,
        "Phone Number Regex" to org.fossify.messages.models.NotificationRuleType.PHONE_NUMBER_REGEX,
        "Message Contains" to org.fossify.messages.models.NotificationRuleType.MESSAGE_CONTAINS,
        "Message Regex" to org.fossify.messages.models.NotificationRuleType.MESSAGE_REGEX
    )
    val categories by viewModel.categories.collectAsState(initial = emptyList())
    val rules by viewModel.rules.collectAsState(initial = emptyList())

    var selectedRuleTypeIndex by remember { mutableStateOf(0) }
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    var valueInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Add New Notification Rule")

        // Rule type dropdown
        var isRuleTypeExpanded by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
            .clickable { isRuleTypeExpanded = true }, verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Rule Type:", modifier = Modifier
                    .padding(end = 8.dp)
            )
            DropdownMenu(
                expanded = isRuleTypeExpanded,
                onDismissRequest = { isRuleTypeExpanded = false },
                modifier = Modifier
                    .width(200.dp)
            ) {
                ruleTypes.forEachIndexed { index, (label, _) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            selectedRuleTypeIndex = index
                            isRuleTypeExpanded = false
                        }
                    )
                }
            }
            Text(ruleTypes[selectedRuleTypeIndex].first, modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Category dropdown
        var isCategoriesExpanded by remember { mutableStateOf(false) }
        Row(modifier = Modifier.clickable { isCategoriesExpanded = true }, verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Category:", modifier = Modifier
                    .padding(end = 8.dp)
            )
            DropdownMenu(
                expanded = isCategoriesExpanded,
                onDismissRequest = { isCategoriesExpanded = false },
                modifier = Modifier
                    .width(200.dp)
            ) {
                categories.forEachIndexed { index, category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            selectedCategoryIndex = index
                            isCategoriesExpanded = false
                        }
                    )
                }
            }
            if (categories.isNotEmpty()) {
                Text(categories[selectedCategoryIndex].name, modifier = Modifier.padding(start = 8.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = valueInput,
            onValueChange = { valueInput = it },
            label = { Text("Value to Match (Phone/Regex/Text)") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (valueInput.isNotBlank() && categories.isNotEmpty()) {
                val ruleType = ruleTypes[selectedRuleTypeIndex].second
                val categoryId = categories[selectedCategoryIndex].id
                viewModel.addNotificationRule(ruleType, valueInput, categoryId)
                valueInput = ""
            }
        }) {
            Text("Add Rule")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Current Rules (Count: ${rules.size}) - Drag to reorder")
        
        // Create a mutable state list for reordering
        val reorderableRules = remember(rules) { rules.toMutableStateList() }
        var isDragInProgress by remember { mutableStateOf(false) }
        
        val lazyListState = androidx.compose.foundation.lazy.rememberLazyListState()
        val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
            reorderableRules.apply {
                add(to.index, removeAt(from.index))
            }
            isDragInProgress = true
        }
        
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(reorderableRules.size, key = { reorderableRules[it].id }) { index ->
                val rule = reorderableRules[index]
                ReorderableItem(reorderableLazyListState, key = rule.id) { isDraggingItem ->
                    // When an item stops being dragged, persist the new order
                    if (!isDraggingItem && isDragInProgress) {
                        isDragInProgress = false
                        viewModel.updateRuleOrders(reorderableRules)
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Drag handle",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .longPressDraggableHandle()
                        )
                        Text(
                            "ID: ${rule.id}, Type: ${rule.ruleType}, Value: ${rule.valueToMatch}, Category: ${rule.categoryId}, Position: $index",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { viewModel.deleteNotificationRule(rule) }) {
                            Text("Delete")
                        }
                    }
                }
            }
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
    // Preview with mock data - in real app, Koin provides the ViewModel
    MaterialTheme {
        // Note: Preview doesn't use actual ViewModel due to Koin requirement
        // This is just for visual preview
    }
}

@Preview(showBackground = true)
@Composable
private fun RulesTabContentPreview() {
    // Preview with mock data - in real app, Koin provides the ViewModel
    MaterialTheme {
        // Note: Preview doesn't use actual ViewModel due to Koin requirement
        // This is just for visual preview
    }
}

@Preview(showBackground = true)
@Composable
private fun ChannelsTabContentPreview() {
    // Preview with mock data - in real app, Koin provides the ViewModel
    MaterialTheme {
        // Note: Preview doesn't use actual ViewModel due to Koin requirement
        // This is just for visual preview
    }
}
