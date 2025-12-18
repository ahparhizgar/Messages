# Dependency Injection Migration to Koin

This document describes the migration from the custom DI pattern to Koin and how to use the new testable architecture.

## Overview

The project has been migrated from a custom dependency injection pattern (using Kotlin extension properties on `Context`) to **Koin 4.1.1**, a modern dependency injection framework for Kotlin. This migration improves testability by introducing interfaces for Android-dependent components.

## Architecture Changes

### Before: Custom DI Pattern
```kotlin
// Context extensions provided dependencies
val Context.config: Config
    get() = Config.newInstance(applicationContext)

val Context.messagesDB: MessagesDao
    get() = getMessagesDB().MessagesDao()

// Usage in Activities
class SettingsActivity : SimpleActivity() {
    private fun someMethod() {
        val setting = config.showCharacterCounter
        messagesDB.insert(message)
    }
}
```

### After: Koin-Based DI
```kotlin
// Interfaces for testability
interface PreferencesProvider {
    var showCharacterCounter: Boolean
    // ... other properties
}

// Koin modules provide implementations
val appModule = module {
    single<PreferencesProvider> { PreferencesProviderImpl(androidContext()) }
}

// Usage with Koin
class MyViewModel(
    private val prefs: PreferencesProvider,
    private val messagesDao: MessagesDao
) : ViewModel() {
    fun doSomething() {
        val setting = prefs.showCharacterCounter
    }
}
```

## Key Components

### 1. Interfaces

#### PreferencesProvider
Abstracts SharedPreferences access for application settings:
- Location: `app/src/main/kotlin/org/fossify/messages/providers/PreferencesProvider.kt`
- Implementation: `PreferencesProviderImpl.kt` (wraps existing `Config` class)
- Test Mock: `app/src/test/kotlin/org/fossify/messages/providers/MockPreferencesProvider.kt`

#### DatabaseProvider
Provides access to Room database DAOs:
- Location: `app/src/main/kotlin/org/fossify/messages/providers/DatabaseProvider.kt`
- Implementation: `DatabaseProviderImpl.kt` (wraps existing `MessagesDatabase`)

### 2. Koin Modules

#### App Module (`di/AppModule.kt`)
Provides application-level dependencies:
- `PreferencesProvider`
- `DatabaseProvider`

#### Data Module (`di/DataModule.kt`)
Provides database DAOs:
- `ConversationsDao`
- `MessagesDao`
- `AttachmentsDao`
- `NotificationRuleDao`
- `NotificationCategoryDao`
- etc.

#### ViewModel Module (`di/ViewModelModule.kt`)
Provides ViewModels with automatic dependency injection:
- `NotificationRulesViewModel`

### 3. Application Initialization

Koin is initialized in the `App` class:
```kotlin
class App : FossifyApp() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                appModule,
                dataModule,
                viewModelModule
            )
        }
    }
}
```

## Using Koin in Your Code

### In Compose Activities

```kotlin
class NotificationRulesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Use koinViewModel() in Compose
            val viewModel: NotificationRulesViewModel = koinViewModel()
            NotificationRulesScreen(viewModel)
        }
    }
}
```

### In Traditional Activities

```kotlin
class MyActivity : SimpleActivity() {
    // Inject dependencies
    private val prefsProvider: PreferencesProvider by inject()
    private val messagesDao: MessagesDao by inject()
    
    private fun someMethod() {
        val setting = prefsProvider.showCharacterCounter
        messagesDao.getAll()
    }
}
```

### In ViewModels

ViewModels receive dependencies through constructor injection:
```kotlin
class MyViewModel(
    private val prefs: PreferencesProvider,
    private val notificationRuleDao: NotificationRuleDao
) : ViewModel() {
    // Use dependencies
}

// Define in viewModelModule
val viewModelModule = module {
    viewModel { MyViewModel(get(), get()) }
}
```

### In Regular Classes

```kotlin
class MyHelper(
    private val prefs: PreferencesProvider
) {
    // Implementation
}

// Define in a module
val helperModule = module {
    factory { MyHelper(get()) }
}
```

## Testing with Koin

### Unit Testing ViewModels

```kotlin
class NotificationRulesViewModelTest : KoinTest {
    private lateinit var testModule: Module
    
    @Before
    fun setup() {
        // Create test module with mocks
        testModule = module {
            single<PreferencesProvider> { MockPreferencesProvider() }
            single<NotificationRuleDao> { mockk<NotificationRuleDao>() }
            single<NotificationCategoryDao> { mockk<NotificationCategoryDao>() }
        }
        
        // Start Koin with test modules
        startKoin {
            modules(testModule)
        }
    }
    
    @After
    fun tearDown() {
        stopKoin()
    }
    
    @Test
    fun testAddRule() {
        // Get ViewModel with mocked dependencies
        val viewModel = NotificationRulesViewModel(get(), get())
        
        // Test implementation
        viewModel.addNotificationRule(
            NotificationRuleType.PHONE_NUMBER_EXACT,
            "123456",
            1L
        )
        
        // Verify behavior
        verify { get<NotificationRuleDao>().insert(any()) }
    }
}
```

### Using MockPreferencesProvider

The `MockPreferencesProvider` allows testing without Android dependencies:

```kotlin
@Test
fun testPreferences() {
    val mockPrefs = MockPreferencesProvider()
    
    // Test preferences
    mockPrefs.showCharacterCounter = true
    assert(mockPrefs.showCharacterCounter)
    
    mockPrefs.addBlockedKeyword("spam")
    assert(mockPrefs.blockedKeywords.contains("spam"))
}
```

## Backward Compatibility

The existing Context extension properties are still available and functional. They continue to work alongside the new Koin-based approach, allowing for gradual migration.

```kotlin
// Old way - still works
val setting = context.config.showCharacterCounter

// New way - preferred for testability
class MyClass(private val prefs: PreferencesProvider) {
    val setting = prefs.showCharacterCounter
}
```

## Migration Strategy

1. **Phase 1** (Current): Core infrastructure in place
   - Koin dependencies added
   - Interfaces created for critical components
   - ViewModel injection updated
   - Test mocks available

2. **Phase 2** (Future): Gradual migration
   - Identify classes that need unit testing
   - Refactor to use constructor injection
   - Add interfaces for additional components as needed
   - Write unit tests using Koin Test

3. **Phase 3** (Future): Complete migration
   - All testable classes use Koin injection
   - Comprehensive test coverage
   - Consider deprecating Context extensions

## Benefits

1. **Testability**: Can mock dependencies without Android framework
2. **Loose Coupling**: Components depend on interfaces, not implementations
3. **Maintainability**: Clear dependency graph
4. **Standard Pattern**: Uses industry-standard DI framework
5. **Type Safety**: Compile-time dependency resolution

## Additional Resources

- [Koin Documentation](https://insert-koin.io/)
- [Koin Android Documentation](https://insert-koin.io/docs/reference/koin-android/start)
- [Koin Compose Documentation](https://insert-koin.io/docs/reference/koin-compose/compose)
- [Testing with Koin](https://insert-koin.io/docs/reference/koin-test/testing)
