package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.CreationScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ResultsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ElectionViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ElectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameAppNavigation(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GameAppNavigation(
    viewModel: ElectionViewModel,
    modifier: Modifier = Modifier
) {
    val gameSave by viewModel.gameSave.collectAsState(initial = null)
    val stateProgresses by viewModel.stateProgresses.collectAsState(initial = emptyList())
    val logs by viewModel.logs.collectAsState(initial = emptyList())

    // Counting States (Results Day overlay)
    val isCountingActive by viewModel.isCountingActive.collectAsState(initial = false)
    val countingProgress by viewModel.countingProgress.collectAsState(initial = 0f)
    val countingPlayerSeats by viewModel.countingPlayerSeats.collectAsState(initial = 0)
    val countingOpponents by viewModel.countingOpponentSeats.collectAsState(initial = 0)
    val countingAlliance by viewModel.countingAllianceSeats.collectAsState(initial = 0)
    val countingTicker by viewModel.countingTicker.collectAsState(initial = "")
    val countingCompleted by viewModel.countingCompleted.collectAsState(initial = false)
    val countingSuccess by viewModel.countingSuccess.collectAsState(initial = false)

    // AI Advisor States
    val isAILoading by viewModel.isAILoading.collectAsState(initial = false)
    val aiSpeechResponse by viewModel.aiSpeechResponse.collectAsState(initial = null)

    val activeGame = gameSave

    if (activeGame == null) {
        // Step 1: Character & Party Creation Profile
        CreationScreen(
            onGameStart = { name, age, startingState, partyName, symbol, color, ideology, slogan, manifesto, costumeIndex ->
                viewModel.startNewGame(
                    name = name,
                    age = age,
                    startingState = startingState,
                    partyName = partyName,
                    symbol = symbol,
                    color = color,
                    ideology = ideology,
                    slogan = slogan,
                    manifesto = manifesto,
                    costumeIndex = costumeIndex
                )
            }
        )
    } else {
        if (isCountingActive) {
            // Step 2b: Immersive Results Day Counting HUD
            ResultsScreen(
                game = activeGame,
                progress = countingProgress,
                playerSeatsCount = countingPlayerSeats,
                opponentsSeatsCount = countingOpponents,
                allianceSeatsCount = countingAlliance,
                tickerMessage = countingTicker,
                completed = countingCompleted,
                success = countingSuccess,
                onCloseResults = {
                    viewModel.dismissCounting()
                }
            )
        } else {
            // Step 2a: Tactical Command Dashboard Core Play
            DashboardScreen(
                game = activeGame,
                states = stateProgresses,
                logs = logs,
                isAILoading = isAILoading,
                aiSpeechResponse = aiSpeechResponse,
                onClearAI = { viewModel.clearAIResponse() },
                onHoldRally = { state, security, asp, acc, ass ->
                    viewModel.holdRally(state, security, asp, acc, ass)
                },
                onSocialBlitz = { state, topic ->
                    viewModel.launchSocialMediaBlitz(state, topic)
                },
                onPromiseWelfare = { state, scheme ->
                    viewModel.promiseWelfareScheme(state, scheme)
                },
                onResolveDebate = { option ->
                    viewModel.resolveDebateAnswer(option)
                },
                onAdvanceTurn = {
                    viewModel.advanceWeek()
                },
                onTriggerElection = {
                    viewModel.runElections()
                },
                onResolveCrisis = { scenarioIdx, optionIdx ->
                    viewModel.resolveCrisis(scenarioIdx, optionIdx)
                },
                onResetGame = {
                    viewModel.resetGame()
                }
            )
        }
    }
}
