package org.fossify.messages.viewmodels

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.fossify.messages.interfaces.NotificationCategoryDao
import org.fossify.messages.interfaces.NotificationRuleDao
import org.fossify.messages.models.NotificationRule
import org.fossify.messages.models.NotificationRuleType
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationRulesViewModelTest {

    private lateinit var viewModel: NotificationRulesViewModel
    private lateinit var notificationRuleDao: NotificationRuleDao
    private lateinit var notificationCategoryDao: NotificationCategoryDao
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        notificationRuleDao = mockk(relaxed = true)
        notificationCategoryDao = mockk(relaxed = true)
        
        // Setup default flows
        coEvery { notificationRuleDao.getAllRules() } returns flowOf(emptyList())
        coEvery { notificationCategoryDao.getAllCategories() } returns flowOf(emptyList())
        
        viewModel = NotificationRulesViewModel(notificationRuleDao, notificationCategoryDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateRuleOrders updates order field for each rule`() = runTest {
        // Given: A list of rules in a new order
        val rule1 = NotificationRule(id = 1, ruleType = NotificationRuleType.PHONE_NUMBER_EXACT, valueToMatch = "123", categoryId = 1, order = 0)
        val rule2 = NotificationRule(id = 2, ruleType = NotificationRuleType.MESSAGE_CONTAINS, valueToMatch = "test", categoryId = 1, order = 1)
        val rule3 = NotificationRule(id = 3, ruleType = NotificationRuleType.PHONE_NUMBER_REGEX, valueToMatch = ".*", categoryId = 1, order = 2)
        
        val reorderedRules = listOf(rule3, rule1, rule2) // Reordered list

        // When: updateRuleOrders is called
        viewModel.updateRuleOrders(reorderedRules)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: updateAll should be called with rules having updated order values
        coVerify {
            notificationRuleDao.updateAll(match { updatedRules ->
                updatedRules.size == 3 &&
                updatedRules[0].id == 3L && updatedRules[0].order == 0 &&
                updatedRules[1].id == 1L && updatedRules[1].order == 1 &&
                updatedRules[2].id == 2L && updatedRules[2].order == 2
            })
        }
    }

    @Test
    fun `updateRuleOrders preserves all rule properties except order`() = runTest {
        // Given: A rule with specific properties
        val originalRule = NotificationRule(
            id = 42,
            ruleType = NotificationRuleType.MESSAGE_REGEX,
            valueToMatch = "test.*pattern",
            categoryId = 7,
            order = 5,
            isEnabled = true
        )
        
        val reorderedRules = listOf(originalRule)

        // When: updateRuleOrders is called
        viewModel.updateRuleOrders(reorderedRules)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: All properties should be preserved except order should be 0 (new position)
        coVerify {
            notificationRuleDao.updateAll(match { updatedRules ->
                val updatedRule = updatedRules[0]
                updatedRule.id == 42L &&
                updatedRule.ruleType == NotificationRuleType.MESSAGE_REGEX &&
                updatedRule.valueToMatch == "test.*pattern" &&
                updatedRule.categoryId == 7L &&
                updatedRule.order == 0 && // New order based on position
                updatedRule.isEnabled == true
            })
        }
    }

    @Test
    fun `updateRuleOrders handles empty list`() = runTest {
        // Given: An empty list of rules
        val emptyRules = emptyList<NotificationRule>()

        // When: updateRuleOrders is called
        viewModel.updateRuleOrders(emptyRules)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: updateAll should be called with empty list
        coVerify {
            notificationRuleDao.updateAll(emptyList())
        }
    }
}
