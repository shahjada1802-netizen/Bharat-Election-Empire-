package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ElectionGameDatabase
import com.example.data.api.GeminiApiClient
import com.example.data.entity.GameSave
import com.example.data.entity.StateProgress
import com.example.data.entity.ElectionLog
import com.example.data.model.GameContent
import com.example.data.repository.ElectionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

class ElectionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ElectionRepository
    val gameSave: StateFlow<GameSave?>
    val stateProgresses: StateFlow<List<StateProgress>>
    val logs: StateFlow<List<ElectionLog>>

    init {
        val database = ElectionGameDatabase.getDatabase(application)
        repository = ElectionRepository(database.electionGameDao())
        
        gameSave = repository.gameSave.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
        stateProgresses = repository.stateProgresses.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        logs = repository.logs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    // --- Dynamic Counting States (Temporary, UI specific) ---
    private val _isCountingActive = MutableStateFlow(false)
    val isCountingActive: StateFlow<Boolean> = _isCountingActive.asStateFlow()

    private val _countingProgress = MutableStateFlow(0f)
    val countingProgress: StateFlow<Float> = _countingProgress.asStateFlow()

    private val _countingPlayerSeats = MutableStateFlow(0)
    val countingPlayerSeats: StateFlow<Int> = _countingPlayerSeats.asStateFlow()

    private val _countingOpponentSeats = MutableStateFlow(0)
    val countingOpponentSeats: StateFlow<Int> = _countingOpponentSeats.asStateFlow()

    private val _countingAllianceSeats = MutableStateFlow(0)
    val countingAllianceSeats: StateFlow<Int> = _countingAllianceSeats.asStateFlow()

    private val _countingTicker = MutableStateFlow("Awaiting live feed from counts...")
    val countingTicker: StateFlow<String> = _countingTicker.asStateFlow()

    private val _countingCompleted = MutableStateFlow(false)
    val countingCompleted: StateFlow<Boolean> = _countingCompleted.asStateFlow()

    private val _countingSuccess = MutableStateFlow(false)
    val countingSuccess: StateFlow<Boolean> = _countingSuccess.asStateFlow()

    // --- AI/Gemini Advisor States ---
    private val _isAILoading = MutableStateFlow(false)
    val isAILoading: StateFlow<Boolean> = _isAILoading.asStateFlow()

    private val _aiSpeechResponse = MutableStateFlow<String?>(null)
    val aiSpeechResponse: StateFlow<String?> = _aiSpeechResponse.asStateFlow()

    // Clear AI responses
    fun clearAIResponse() {
        _aiSpeechResponse.value = null
    }

    // --- Core Operations ---

    fun startNewGame(
        name: String,
        age: Int,
        startingState: String,
        partyName: String,
        symbol: String,
        color: String,
        ideology: String,
        slogan: String,
        manifesto: String,
        costumeIndex: Int
    ) {
        viewModelScope.launch {
            // Log wipe out
            repository.clearAll()

            // Initialize GameSave
            val initialGame = GameSave(
                playerName = name,
                playerAge = age,
                playerState = startingState,
                characterStyle = costumeIndex,
                partyName = partyName,
                partySymbol = symbol,
                partyColor = color,
                ideology = ideology,
                slogan = slogan,
                manifestoFocus = manifesto,
                funds = 25.0, // ₹25 Lakhs core starter fund for MLA campaign
                fame = 20,
                charisma = 5,
                speakingSkill = 5,
                socialMediaPopularity = 15,
                corruptionScandalIndex = 0,
                currentTurn = 1,
                currentYear = 2026,
                careerStage = "MLA",
                activeElectionStateName = startingState
            )
            repository.saveGame(initialGame)

            // Setup states data
            val initialStates = GameContent.initializeStates()
            repository.insertStateProgressList(initialStates)

            // Initial logs
            repository.insertLog(ElectionLog(
                tag = "MILESTONE",
                title = "Party Formed: $partyName",
                details = "Leader $name has registered a new political ideology ($ideology) to challenge typical power bases in India! High-octane campaign starts now."
            ))
        }
    }

    // Hold massive rally
    fun holdRally(
        stateName: String,
        crowdSecurity: String, // "Standard", "Premium", "VIP VIP Security"
        speechAspiration: String,
        speechAccusation: String,
        speechAssurance: String
    ) {
        viewModelScope.launch {
            val game = repository.getGameSaveSync() ?: return@launch
            val progressList = stateProgresses.value
            val stateProgress = progressList.find { it.stateName == stateName } ?: return@launch

            // Costs logic
            val baseCost = when (crowdSecurity) {
                "VIP" -> 12.0 // lakh
                "Premium" -> 6.0
                else -> 2.5
            }

            if (game.funds < baseCost) {
                repository.insertLog(ElectionLog(
                    tag = "CAMPAIGN",
                    title = "Rally Cancelled",
                    details = "Insufficent campaign cash to book local grounds for rally in $stateName!"
                ))
                return@launch
            }

            // Weather check
            var weatherModifier = 1.0
            var weatherNote = ""
            when (stateProgress.weather) {
                "Monsoon" -> {
                    if (crowdSecurity != "VIP" && crowdSecurity != "Premium") {
                        weatherModifier = 0.4
                        weatherNote = " Severe monsoon rain disrupted typical flat tents. Poor turnout recorded."
                    } else {
                        weatherModifier = 0.95
                        weatherNote = " High-scale waterproof shelters secured crowds despite continuous rain."
                    }
                }
                "Heatwave" -> {
                    if (crowdSecurity != "VIP") {
                        weatherModifier = 0.65
                        weatherNote = " Heatwave temperature kept senior citizens indoors. Water stall arrangements were helpful."
                    } else {
                        weatherModifier = 1.1
                        weatherNote = " Dedicated AC fans and cold hydration channels drew massive active families!"
                    }
                }
                "Cyclone" -> {
                    weatherModifier = 0.15
                    weatherNote = " National disaster alarm sounded. Rallies highly restricted."
                }
                else -> {
                    weatherModifier = 1.2
                    weatherNote = " Perfect bright autumn conditions. Tremendous local cheer!"
                }
            }

            // Calculations
            val effectiveCharisma = game.charisma * 1.5
            val basicAttendance = (Random.nextInt(5000, 30000) * weatherModifier).roundToInt()
            val baseBoost = (basicAttendance / 6000.0) + (effectiveCharisma * 0.4)
            
            val doubleBoost = baseBoost.coerceAtMost(18.0) // Support cap per rally

            // Adjust State variables
            val finalPlayerSupport = (stateProgress.playerSupportRate + doubleBoost).coerceAtMost(95.0)
            val leftOver = 100.0 - finalPlayerSupport
            // Adjust opposition support proportionally
            val finalOppositionSupport = (stateProgress.oppositionSupportRate / (stateProgress.oppositionSupportRate + stateProgress.undecidedSupportRate)) * leftOver
            val finalUndecidedSupport = leftOver - finalOppositionSupport

            // Update Database
            val updatedProgress = stateProgress.copy(
                playerSupportRate = Math.round(finalPlayerSupport * 100.0) / 100.0,
                oppositionSupportRate = Math.round(finalOppositionSupport * 100.0) / 100.0,
                undecidedSupportRate = Math.round(finalUndecidedSupport * 100.0) / 100.0,
                ralliesHeld = stateProgress.ralliesHeld + 1
            )
            repository.insertStateProgress(updatedProgress)

            // Update GameSave
            val updatedSave = game.copy(
                funds = game.funds - baseCost,
                fame = (game.fame + 4).coerceAtMost(100),
                speakingSkill = (game.speakingSkill + 1).coerceAtMost(10)
            )
            repository.saveGame(updatedSave)

            // Dynamic Gemini or template headline
            _isAILoading.value = true
            val systemPrompt = "You are a realistic, senior political analyst and reporter for 'Bharat Times news'. Provide a catchy, realistic news headline and dynamic 2-sentence story about a local political rally."
            val userPrompt = "Political leader '${game.playerName}' of party '${game.partyName}' (Ideology: ${game.ideology}, Slogan: ${game.slogan}) held a mega rally in state of $stateName during a '${stateProgress.weather}' weather spell. The leader spoke about: '$speechAspiration', accused opponent of: '$speechAccusation' and promised: '$speechAssurance'. Attendance estimated at $basicAttendance people. Keep the news slightly cinematic but professional."
            
            val contentResponse = GeminiApiClient.getGeminiResponse(systemPrompt, userPrompt)
            val logDetails = contentResponse ?: "MEGA RALLY: '${game.partyName}' leader captures attention of $basicAttendance citizens in $stateName! Speeches targeted rivals with high conviction.$weatherNote"
            _aiSpeechResponse.value = logDetails

            _isAILoading.value = false

            repository.insertLog(ElectionLog(
                tag = "CAMPAIGN",
                title = "Crowd of ${basicAttendance.toString().format()} Held in $stateName",
                details = logDetails,
                stateAffected = stateName
            ))
        }
    }

    // Launch Social Media Blitz
    fun launchSocialMediaBlitz(stateName: String, sloganTopic: String) {
        viewModelScope.launch {
            val game = repository.getGameSaveSync() ?: return@launch
            val progressList = stateProgresses.value
            val stateProgress = progressList.find { it.stateName == stateName } ?: return@launch

            val mediaCost = 1.8 // ₹1.8 Lakh budget
            if (game.funds < mediaCost) return@launch

            // Online multiplier based on current digital fame
            val digitalFactor = game.socialMediaPopularity / 25.0 + 1.2
            val supportIncrease = (Random.nextDouble(2.0, 5.5) * digitalFactor)

            val finalPlayerSupport = (stateProgress.playerSupportRate + supportIncrease).coerceAtMost(90.0)
            val leftOver = 100.0 - finalPlayerSupport
            val finalOppositionSupport = (stateProgress.oppositionSupportRate / (stateProgress.oppositionSupportRate + stateProgress.undecidedSupportRate)) * leftOver
            val finalUndecidedSupport = leftOver - finalOppositionSupport

            repository.insertStateProgress(stateProgress.copy(
                playerSupportRate = Math.round(finalPlayerSupport * 100.0) / 100.0,
                oppositionSupportRate = Math.round(finalOppositionSupport * 100.0) / 100.0,
                undecidedSupportRate = Math.round(finalUndecidedSupport * 100.0) / 100.0
            ))

            val updatedSave = game.copy(
                funds = game.funds - mediaCost,
                socialMediaPopularity = (game.socialMediaPopularity + 5).coerceAtMost(100),
                fame = (game.fame + 2).coerceAtMost(100)
            )
            repository.saveGame(updatedSave)

            // Log online surge
            val hashTag = "#" + game.partyName.filter { !it.isWhitespace() } + sloganTopic.filter { !it.isWhitespace() }
            repository.insertLog(ElectionLog(
                tag = "NEWS",
                title = "$hashTag Trends Nationwide!",
                details = "Online algorithms indicate a massive digital surge in $stateName! Young voters are heavily retweeting ${game.playerName}'s recent digital quote: '${game.slogan}'.",
                stateAffected = stateName
            ))
        }
    }

    // Promise welfare scheme
    fun promiseWelfareScheme(stateName: String, schemeType: String) {
        viewModelScope.launch {
            val game = repository.getGameSaveSync() ?: return@launch
            val progressList = stateProgresses.value
            val stateProgress = progressList.find { it.stateName == stateName } ?: return@launch

            val initialPromiseCost = 3.5 // Promise setup & catalog
            if (game.funds < initialPromiseCost) return@launch

            // Huge boost but minor scandal index increment due to opponents calling it 'freebie culture'
            val supportIncrease = when (schemeType) {
                "Farmers Direct Payout" -> if (stateProgress.ruralPercent > 60) 8.0 else 4.0
                "Free Wifi & Laptops" -> if (stateProgress.urbanPercent > 40) 7.5 else 3.5
                "Pension & Ration Support" -> 6.0
                else -> 4.5
            }

            val finalPlayerSupport = (stateProgress.playerSupportRate + supportIncrease).coerceAtMost(90.0)
            val leftOver = 100.0 - finalPlayerSupport
            val finalOppositionSupport = (stateProgress.oppositionSupportRate / (stateProgress.oppositionSupportRate + stateProgress.undecidedSupportRate)) * leftOver
            val finalUndecidedSupport = leftOver - finalOppositionSupport

            // Scandal ticks up slightly for populist measures in urban papers
            val scandalTick = if (schemeType == "Farmers Direct Payout") 3 else 1

            repository.insertStateProgress(stateProgress.copy(
                playerSupportRate = Math.round(finalPlayerSupport * 100.0) / 100.0,
                oppositionSupportRate = Math.round(finalOppositionSupport * 100.0) / 100.0,
                undecidedSupportRate = Math.round(finalUndecidedSupport * 100.0) / 100.0,
                welfareSchemesPromised = stateProgress.welfareSchemesPromised + 1
            ))

            val updatedSave = game.copy(
                funds = game.funds - initialPromiseCost,
                corruptionScandalIndex = (game.corruptionScandalIndex + scandalTick).coerceAtMost(100),
                fame = (game.fame + 5).coerceAtMost(100)
            )
            repository.saveGame(updatedSave)

            repository.insertLog(ElectionLog(
                tag = "CAMPAIGN",
                title = "Welfare Promise Launched in $stateName",
                details = "Leader ${game.playerName} promised structural '$schemeType' implementation if elected. Critics debate the economic stability, but core community groups express huge joy!",
                stateAffected = stateName
            ))
        }
    }

    // TV Debate Answer Resolution
    fun resolveDebateAnswer(option: GameContent.DebateOption) {
        viewModelScope.launch {
            val game = repository.getGameSaveSync() ?: return@launch
            val activeState = game.activeElectionStateName ?: game.playerState
            val progressList = stateProgresses.value
            val stateProgress = progressList.find { it.stateName == activeState } ?: return@launch

            // Resolve factors
            val updatedFunds = (game.funds + option.fundsEffect).coerceAtLeast(0.0)
            val updatedFame = (game.fame + option.popularityEffect).coerceIn(0, 100)
            val updatedScandal = (game.corruptionScandalIndex + option.scandalEffect).coerceIn(0, 100)
            val updatedCharisma = (game.charisma + if (option.popularityEffect > 10) 1 else 0).coerceIn(1, 10)

            val finalPlayerSupport = (stateProgress.playerSupportRate + option.supportEffect).coerceIn(1.0, 95.0)
            val leftOver = 100.0 - finalPlayerSupport
            val finalOppositionSupport = (stateProgress.oppositionSupportRate / (stateProgress.oppositionSupportRate + stateProgress.undecidedSupportRate)) * leftOver
            val finalUndecidedSupport = leftOver - finalOppositionSupport

            // Save Progress
            repository.insertStateProgress(stateProgress.copy(
                playerSupportRate = Math.round(finalPlayerSupport * 100.0) / 100.0,
                oppositionSupportRate = Math.round(finalOppositionSupport * 100.0) / 100.0,
                undecidedSupportRate = Math.round(finalUndecidedSupport * 100.0) / 100.0
            ))

            // Save Game
            repository.saveGame(game.copy(
                funds = updatedFunds,
                fame = updatedFame,
                corruptionScandalIndex = updatedScandal,
                charisma = updatedCharisma,
                currentTurn = game.currentTurn + 1
            ))

            repository.insertLog(ElectionLog(
                tag = "NEWS",
                title = "Clash at PrimeTime Debate Studio",
                details = "${game.playerName} answered query with high dynamic flair: \"${option.responseDialog}\". Public analysis indicates direct sentiment changes.",
                stateAffected = activeState
            ))
        }
    }

    // End Turn (Advance week)
    fun advanceWeek() {
        viewModelScope.launch {
            val game = repository.getGameSaveSync() ?: return@launch
            
            // Generate some random campaign funds/donations based on fame
            val donationAmount = (game.fame * 0.15) + Random.nextDouble(1.0, 4.0)
            val updatedFunds = game.funds + donationAmount

            // Opponent holds rally or triggers events
            val progressList = stateProgresses.value
            var turnEventNote = ""
            var affectedStateName = ""
            
            if (progressList.isNotEmpty()) {
                val targetState = progressList.random()
                affectedStateName = targetState.stateName
                
                // Rival rally cuts player's support slightly or claims undecided
                val opponentBoost = Random.nextDouble(1.0, 4.5)
                val newOppo = (targetState.oppositionSupportRate + opponentBoost).coerceAtMost(90.0)
                val leftOver = 100.0 - newOppo
                val newPlayer = (targetState.playerSupportRate / (targetState.playerSupportRate + targetState.undecidedSupportRate)) * leftOver
                val newUndecided = leftOver - newPlayer

                repository.insertStateProgress(targetState.copy(
                    oppositionSupportRate = Math.round(newOppo * 100.0) / 100.0,
                    playerSupportRate = Math.round(newPlayer * 100.0) / 100.0,
                    undecidedSupportRate = Math.round(newUndecided * 100.0) / 100.0
                ))

                turnEventNote = "Opposition alliance launched aggressive padayatra marches across rural sectors in $affectedStateName, pulling some undecided support."
            }

            // Update save
            repository.saveGame(game.copy(
                currentTurn = game.currentTurn + 1,
                funds = Math.round(updatedFunds * 100.0) / 100.0
            ))

            // Log event
            repository.insertLog(ElectionLog(
                tag = "MILESTONE",
                title = "Campaign Week ${game.currentTurn} Concludes",
                details = "Donation cells generated +₹${String.format("%.2f", donationAmount)} Lakhs based on public rating. $turnEventNote",
                stateAffected = affectedStateName
            ))
        }
    }

    // --- Dynamic Counting & Results Day Simulation (Immersive Coroutine Engine) ---
    fun runElections() {
        viewModelScope.launch {
            val game = repository.getGameSaveSync() ?: return@launch
            
            // Start the dramatic counting procedure
            _isCountingActive.value = true
            _countingProgress.value = 0f
            _countingPlayerSeats.value = 0
            _countingOpponentSeats.value = 0
            _countingAllianceSeats.value = 0
            _countingCompleted.value = false
            _countingSuccess.value = false
            _countingTicker.value = "EVMS SECURED. Regional counting offices opening across constituencies..."

            delay(2000)

            val trackingStates = stateProgresses.value
            if (game.careerStage == "MLA") {
                // Focus state
                val startingState = game.playerState
                val progress = trackingStates.find { it.stateName == startingState } ?: trackingStates.first()
                
                _countingTicker.value = "Counting started for Assembly seat in $startingState. High voter turnout recorded!"
                _countingProgress.value = 0.3f
                delay(2000)

                val playerScore = progress.playerSupportRate
                val oppositionScore = progress.oppositionSupportRate + Random.nextDouble(-5.0, 5.0)
                
                _countingTicker.value = "Final rounds of EVM verification in progress. Close neck-and-neck fight..."
                _countingProgress.value = 0.7f
                delay(2000)

                val won = playerScore > oppositionScore
                val playerVotes = (Random.nextInt(85000, 140000) * (playerScore / 100.0)).toInt()
                val oppVotes = (Random.nextInt(85000, 140000) * (oppositionScore / 100.0)).toInt()

                _countingProgress.value = 1.0f
                _countingCompleted.value = true
                _countingSuccess.value = won

                if (won) {
                    _countingTicker.value = "VICTORY! ${game.playerName} wins the MLA assembly seat by ${playerVotes - oppVotes} votes!"
                    val updatedSave = game.copy(
                        mlaAssemblyWon = true,
                        mlaVotesReceived = playerVotes,
                        mlaOpponentVotes = oppVotes,
                        // Level up to Chief Minister campaigns (gives big starting campaign funds of ₹15 Crores)!
                        careerStage = "CM",
                        funds = 1500.0, // ₹1,500 Lakhs = ₹15 Crores
                        fame = 50,
                        currentTurn = 1,
                        activeElectionStateName = startingState
                    )
                    repository.saveGame(updatedSave)
                    repository.insertLog(ElectionLog(
                        tag = "MILESTONE",
                        title = "Elected as MLA in $startingState Assembly!",
                        details = "Massive celebration sweeps through constituents! ${game.playerName} won by securing $playerVotes votes. High expectations await this new legislative career!"
                    ))
                } else {
                    _countingTicker.value = "DEFEAT. Opponent alliance secures the MLA seat. Recalibrating party strategy..."
                    repository.insertLog(ElectionLog(
                        tag = "SCANDAL",
                        title = "MLA Election Defeat in $startingState",
                        details = "With only ${playerScore.roundToInt()}% support, the local mandate slipped into the opposition's court. We need bigger rallies next round."
                    ))
                }

            } else if (game.careerStage == "CM") {
                // Chief Minister: state-wide assembly seats
                val activeState = game.activeElectionStateName ?: game.playerState
                val progress = trackingStates.find { it.stateName == activeState } ?: trackingStates.first()
                val totalSeats = progress.assemblySeats
                val targetToWin = totalSeats / 2 + 1

                _countingTicker.value = "Statewide scanning for $totalSeats Legislative Assembly seats in $activeState!"
                
                var currentCounted = 0
                while (currentCounted < totalSeats) {
                    delay(150)
                    val chunk = (totalSeats / 20).coerceAtLeast(1)
                    currentCounted = (currentCounted + chunk).coerceAtMost(totalSeats)
                    
                    // Seat winner simulation based on state-wide percentages
                    val playerWinProb = progress.playerSupportRate / 100.0
                    val localRandom = Random.nextDouble()
                    if (localRandom < playerWinProb) {
                        _countingPlayerSeats.value = _countingPlayerSeats.value + chunk
                    } else if (localRandom < playerWinProb + 0.15) {
                        // Regional independent / friendly partners
                        _countingAllianceSeats.value = _countingAllianceSeats.value + chunk
                    } else {
                        _countingOpponentSeats.value = _countingOpponentSeats.value + chunk
                    }

                    _countingProgress.value = (currentCounted.toFloat() / totalSeats) * 0.9f
                    _countingTicker.value = "Counting trends updated: Private exit polls shifting. Current seats declared: $currentCounted / $totalSeats"
                }

                // Balance remaining to match actual exact total
                val totalDecls = _countingPlayerSeats.value + _countingOpponentSeats.value + _countingAllianceSeats.value
                if (totalDecls != totalSeats) {
                    val diff = totalSeats - totalDecls
                    if (diff > 0) {
                        _countingOpponentSeats.value = _countingOpponentSeats.value + diff
                    }
                }

                _countingProgress.value = 1.0f
                _countingCompleted.value = true

                val playerSeats = _countingPlayerSeats.value
                val isAbsoluteMajority = playerSeats >= targetToWin
                // Coalition possibility if we are the single largest party (> opposition and we can alliance with neutrals)
                val isCoalitionViable = !isAbsoluteMajority && (playerSeats + _countingAllianceSeats.value >= targetToWin) && (playerSeats > _countingOpponentSeats.value)

                val wonGroup = isAbsoluteMajority || isCoalitionViable
                _countingSuccess.value = wonGroup

                if (wonGroup) {
                    val coalitionMsg = if (isAbsoluteMajority) "Absolute Majority Government!" else "Coalition government formed with regional alliance partners!"
                    _countingTicker.value = "CHIEF MINISTER DECLARED! $coalitionMsg ($playerSeats / $totalSeats seats won)"
                    
                    // Leap to Prime Minister campaigns nationally with base funds of ₹100 Crores
                    val updatedSave = game.copy(
                        cmSeatsWon = playerSeats,
                        cmTotalSeatsInState = totalSeats,
                        cmCoalitionFormed = !isAbsoluteMajority,
                        careerStage = "PM",
                        funds = 10000.0, // ₹10,000 Lakhs = ₹100 Crores
                        fame = 75,
                        currentTurn = 1,
                        activeElectionStateName = "All India"
                    )
                    repository.saveGame(updatedSave)
                    
                    repository.insertLog(ElectionLog(
                        tag = "MILESTONE",
                        title = "Oath of Chief Minister in $activeState!",
                        details = "History is written. ${game.playerName} is officially sworn in as the Chief Minister. The cabinet plans mega welfare measures. Upward vision towards Lok Sabha!"
                    ))
                } else {
                    _countingTicker.value = "ELECTION LOSS in $activeState. Failed to reach majority seat count of $targetToWin."
                    repository.insertLog(ElectionLog(
                        tag = "SCANDAL",
                        title = "Government Mandate Lost in $activeState",
                        details = "Secured only $playerSeats of $totalSeats seats. Opponents managed to capture the chief secretariat. We must expand dynamic digital campaigning."
                    ))
                }

            } else if (game.careerStage == "PM") {
                // National PM Elections: Lok Sabha (543 total seats)
                val totalLokSabha = 543
                val targetLS = 272

                _countingTicker.value = "National General Elections LIVE: Scanning counting centers across 28 states and UTs!"
                
                var seatsAnalyzed = 0
                while (seatsAnalyzed < totalLokSabha) {
                    delay(120)
                    val chunk = 25
                    seatsAnalyzed = (seatsAnalyzed + chunk).coerceAtMost(totalLokSabha)

                    // Average support across all progress states in database
                    val avgPlayerSupport = trackingStates.map { it.playerSupportRate }.average().takeIf { !it.isNaN() } ?: 15.0
                    val nationalWinProb = avgPlayerSupport / 100.0
                    
                    val roll = Random.nextDouble()
                    if (roll < nationalWinProb) {
                        _countingPlayerSeats.value = _countingPlayerSeats.value + chunk
                    } else if (roll < nationalWinProb + 0.12) {
                        _countingAllianceSeats.value = _countingAllianceSeats.value + chunk
                    } else {
                        _countingOpponentSeats.value = _countingOpponentSeats.value + chunk
                    }

                    _countingProgress.value = (seatsAnalyzed.toFloat() / totalLokSabha) * 0.9f
                    _countingTicker.value = "Parliament seats streaming: Major leads emerging in North, South, and Coastal grids. Counted: $seatsAnalyzed / 543"
                }

                // Balance logic
                val currentAgg = _countingPlayerSeats.value + _countingOpponentSeats.value + _countingAllianceSeats.value
                if (currentAgg != totalLokSabha) {
                    val diff = totalLokSabha - currentAgg
                    if (diff > 0) {
                        _countingOpponentSeats.value = _countingOpponentSeats.value + diff
                    }
                }

                _countingProgress.value = 1.0f
                _countingCompleted.value = true

                val playerLSSeats = _countingPlayerSeats.value
                val hasLsMajority = playerLSSeats >= targetLS
                val hasLsCoalitionValue = !hasLsMajority && (playerLSSeats + _countingAllianceSeats.value >= targetLS) && (playerLSSeats > _countingOpponentSeats.value)

                val absoluteWin = hasLsMajority || hasLsCoalitionValue
                _countingSuccess.value = absoluteWin

                if (absoluteWin) {
                    val message = if (hasLsMajority) "Absolute single-party parliamentary majority!" else "Historic Lok Sabha grand coalition cabinet formed!"
                    _countingTicker.value = "PRIME MINISTER OF INDIA! $message ($playerLSSeats / 543 Seats)"
                    
                    val updatedSave = game.copy(
                        pmSeatsWon = playerLSSeats,
                        pmTotalSeats = totalLokSabha,
                        pmCoalitionFormed = !hasLsMajority,
                        isInGovtMode = true, // Open government mode after PM win!
                        approvalRating = 72,
                        funds = 50000.0 // Huge Treasury budget ₹500 Crores
                    )
                    repository.saveGame(updatedSave)

                    repository.insertLog(ElectionLog(
                        tag = "MILESTONE",
                        title = "SWEARING IN: ${game.playerName} becomes Prime Minister!",
                        details = "Spectacular visuals. Over 100,000 national invitees cheer as the young leader from humble beginnings takes the oath of Prime Minister at Rashtrapati Bhavan!"
                    ))
                } else {
                    _countingTicker.value = "Lok Sabha defeat. Secured only $playerLSSeats / 543 seats. Opponent forms national government."
                    repository.insertLog(ElectionLog(
                        tag = "SCANDAL",
                        title = "National Election Defeat",
                        details = "Grand coalition of rivals captures Central Secretariat. Recalibrate digital reach, party manifestos, and try a fresh leadership round."
                    ))
                }
            }
        }
    }

    // Acknowledge results (close counting screen)
    fun dismissCounting() {
        _isCountingActive.value = false
        _countingCompleted.value = false
    }

    // Reset whole slot (New Game)
    fun resetGame() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }

    // --- Government mode: Pass dynamic bills / handle crisis ---
    fun resolveCrisis(scenarioIdx: Int, optionIdx: Int) {
        viewModelScope.launch {
            val game = repository.getGameSaveSync() ?: return@launch
            val scenario = GameContent.GOVT_CRISES.getOrNull(scenarioIdx) ?: return@launch
            val option = scenario.options.getOrNull(optionIdx) ?: return@launch

            // Consequences
            val updatedFunds = (game.funds + option.fundsAction).coerceAtLeast(0.0)
            val updatedApproval = (game.approvalRating + option.approvalAction).coerceIn(0, 100)
            val updatedScandal = (game.corruptionScandalIndex + option.scandalAction).coerceIn(0, 100)

            repository.saveGame(game.copy(
                funds = updatedFunds,
                approvalRating = updatedApproval,
                corruptionScandalIndex = updatedScandal,
                currentTurn = game.currentTurn + 1
            ))

            repository.insertLog(ElectionLog(
                tag = "NEWS",
                title = "Cabinet Decision: Resolving Crisis",
                details = option.resultLog
            ))
        }
    }
}
