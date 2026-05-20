package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.GameSave
import com.example.data.entity.StateProgress
import com.example.data.entity.ElectionLog
import com.example.ui.components.IndiaMapRenderer
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.SaffronPrimary
import kotlin.math.roundToInt
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    game: GameSave,
    states: List<StateProgress>,
    logs: List<ElectionLog>,
    isAILoading: Boolean,
    aiSpeechResponse: String?,
    onClearAI: () -> Unit,
    onHoldRally: (state: String, security: String, asp: String, acc: String, ass: String) -> Unit,
    onSocialBlitz: (state: String, topic: String) -> Unit,
    onPromiseWelfare: (state: String, scheme: String) -> Unit,
    onResolveDebate: (com.example.data.model.GameContent.DebateOption) -> Unit,
    onAdvanceTurn: () -> Unit,
    onTriggerElection: () -> Unit,
    onResolveCrisis: (scenarioIdx: Int, optionIdx: Int) -> Unit,
    onResetGame: () -> Unit
) {
    var activeTab by remember { mutableStateOf("Command Map") } // "Command Map", "News Logs", "Cabinet Bills"
    var focusedState by remember { mutableStateOf<StateProgress?>(null) }
    
    // Set focused state to player starting state on default if empty
    LaunchedEffect(states, game) {
        if (focusedState == null && states.isNotEmpty()) {
            focusedState = states.find { it.stateName == game.activeElectionStateName } ?: states.first()
        }
    }

    // Modal forms states
    var showRallySheet by remember { mutableStateOf(false) }
    var showSocialSheet by remember { mutableStateOf(false) }
    var showWelfareSheet by remember { mutableStateOf(false) }
    var showDebateSheet by remember { mutableStateOf(false) }
    var showAdvisoryDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = game.partyName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = SaffronPrimary
                        )
                        Text(
                            text = "Leader: ${game.playerName} | Career Level: ${game.careerStage}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showAdvisoryDialog = true }) {
                        Icon(Icons.Default.VolunteerActivism, contentDescription = "Manifesto info", tint = Color.Yellow)
                    }
                    IconButton(onClick = onResetGame) {
                        Icon(Icons.Default.RestartAlt, contentDescription = "Restart Game", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF101223)
                )
            )
        },
        bottomBar = {
            // Standard Navigation Bar with consistent active indicators
            NavigationBar(
                containerColor = Color(0xFF101223),
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    selected = activeTab == "Command Map",
                    onClick = { activeTab = "Command Map" },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                    label = { Text("Command Map", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f),
                        indicatorColor = Color.White.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = activeTab == "News Logs",
                    onClick = { activeTab = "News Logs" },
                    icon = { Icon(Icons.Default.FormatListBulleted, contentDescription = null) },
                    label = { Text("News Logs", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f),
                        indicatorColor = Color.White.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = activeTab == "Cabinet Bills",
                    onClick = { activeTab = "Cabinet Bills" },
                    icon = { Icon(Icons.Default.Task, contentDescription = null) },
                    label = { Text("Cabinet Mode", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f),
                        indicatorColor = Color.White.copy(alpha = 0.1f)
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF0F111E))
        ) {
            when (activeTab) {
                "Command Map" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(12.dp)
                    ) {
                        // --- Player Stats Banner ---
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatCard(
                                title = "BUDGET TREASURY",
                                value = if (game.careerStage == "PM") "₹${(game.funds / 100.0).roundToInt()} Cr" else "₹${game.funds.roundToInt()} Lk",
                                icon = Icons.Default.CurrencyRupee,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                title = "PUBLIC FAME",
                                value = "${game.fame}%",
                                icon = Icons.Default.TrendingUp,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                title = "REVENUES / WEEK",
                                value = "+₹${(game.fame * 0.15 + 2).roundToInt()} L",
                                icon = Icons.Default.AddBusiness,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // --- Interactive Command Map ---
                        IndiaMapRenderer(
                            states = states,
                            selectedStateName = focusedState?.stateName,
                            onStateSelect = { clicked ->
                                focusedState = clicked
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // --- Focused State HQ Campaign control panel ---
                        focusedState?.let { sState ->
                            val fullStateProgressState = states.find { it.stateName == sState.stateName } ?: sState
                            
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .testTag("campaign_controls_card"),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1E36)),
                                shape = RoundedCornerShape(16.dp),
                                border = ButtonDefaults.outlinedButtonBorder
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "CAMPAIGN HEADQUARTERS: ${fullStateProgressState.stateName.uppercase()}",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = SaffronPrimary
                                            )
                                            Text(
                                                text = "Needs Issue: ${fullStateProgressState.mainIssue} | Local Weather: ${fullStateProgressState.weather}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White.copy(alpha = 0.7f)
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Default.Campaign,
                                            contentDescription = null,
                                            tint = SaffronPrimary,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Support rates meters
                                    Text("Constituency Support Breakdown", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .height(24.dp)
                                            .background(Color(0xFF0F111E), RoundedCornerShape(12.dp))
                                            .padding(horizontal = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val playerS = fullStateProgressState.playerSupportRate.toFloat() / 100f
                                        val oppS = fullStateProgressState.oppositionSupportRate.toFloat() / 100f
                                        val swingS = 1f - playerS - oppS

                                        Box(
                                            modifier = Modifier
                                                .weight(playerS.coerceAtLeast(0.01f))
                                                .fillMaxHeight()
                                                .background(EmeraldGreen, RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("You ${(playerS * 100).roundToInt()}%", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Box(
                                            modifier = Modifier
                                                .weight(oppS.coerceAtLeast(0.01f))
                                                .fillMaxHeight()
                                                .background(Color(0xFF9E2C2C)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("Rivals ${(oppS * 100).roundToInt()}%", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Box(
                                            modifier = Modifier
                                                .weight(swingS.coerceAtLeast(0.01f))
                                                .fillMaxHeight()
                                                .background(Color(0xFF64748B), RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("Swing ${(swingS * 100).roundToInt()}%", color = Color.White, fontSize = 9.sp)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Action buttons grid
                                    Text("Primary Campaign Actions", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SaffronPrimary)
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { showRallySheet = true },
                                            modifier = Modifier.weight(1f).testTag("rally_action_btn"),
                                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(8.dp)
                                        ) {
                                            Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Mega Rally", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = { showSocialSheet = true },
                                            modifier = Modifier.weight(1f).testTag("social_action_btn"),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDF8)),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(8.dp)
                                        ) {
                                            Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Web Blitz", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { showWelfareSheet = true },
                                            modifier = Modifier.weight(1f).testTag("welfare_action_btn"),
                                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(8.dp)
                                        ) {
                                            Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Welfare Scheme", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = { showDebateSheet = true },
                                            modifier = Modifier.weight(1f).testTag("tv_debate_action_btn"),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(8.dp)
                                        ) {
                                            Icon(Icons.Default.Tv, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("TV Debate", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // --- End Week / Call Election Day Card ---
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF101223))
                        ) {
                            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "CAMPAIGN WEEK MANAGEMENT",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = onAdvanceTurn,
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E3440)),
                                        modifier = Modifier.weight(1.2f).height(48.dp).testTag("end_week_btn")
                                    ) {
                                        Icon(Icons.Default.SkipNext, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("End Campaign Week", fontSize = 12.sp)
                                    }

                                    Button(
                                        onClick = onTriggerElection,
                                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                        modifier = Modifier.weight(1f).height(48.dp).testTag("trigger_elections_btn")
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Initiate Counting", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.ExtraBold)
                                    }
                                }
                            }
                        }
                    }
                }

                "News Logs" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            Icon(Icons.Default.Circle, contentDescription = null, tint = Color.Red, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "NATIONAL LIVE BROADCAST TICKER",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(logs) { newsLog ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1E36)),
                                    border = ButtonDefaults.outlinedButtonBorder
                                ) {
                                    Row(modifier = Modifier.padding(12.dp)) {
                                        val bulletColor = when (newsLog.tag) {
                                            "MILESTONE" -> Color.Yellow
                                            "NEWS" -> SaffronPrimary
                                            "CAMPAIGN" -> EmeraldGreen
                                            else -> Color.Red
                                        }
                                        Box(
                                            modifier = Modifier
                                                .padding(top = 4.dp)
                                                .size(8.dp)
                                                .background(bulletColor, RoundedCornerShape(50))
                                        )

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                Text(
                                                    text = newsLog.title,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = Color.White,
                                                    modifier = Modifier.weight(1.5f)
                                                )
                                                Text(
                                                    text = if (newsLog.stateAffected != null) "#${newsLog.stateAffected}" else "",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = SaffronPrimary,
                                                    modifier = Modifier.weight(1f).fillMaxWidth(),
                                                    textAlign = TextAlign.End
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = newsLog.details,
                                                fontSize = 12.sp,
                                                color = Color.White.copy(alpha = 0.8f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "Cabinet Bills" -> {
                    var activeCrisisVal by remember { mutableStateOf(0) }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            "CABINET GOVERNMENT ADJUDICATION",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = SaffronPrimary
                        )
                        Text(
                            "As an executive representative, manage major crises, stabilize national approval and direct expenditure bills.",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        if (!game.isInGovtMode) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF1B1E36), RoundedCornerShape(12.dp))
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "🚨 GOVERNMENT MODES RESTRICTED\n\nWin national PM general elections across Lok Sabha seats first to enact central bills and control emergency relief actions!",
                                    color = Color.White.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            // Government level dashboard
                            Text("Approval Rating: ${game.approvalRating}%", fontWeight = FontWeight.Bold, color = EmeraldGreen, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))

                            // Crisis Options
                            val activeCrisis = com.example.data.model.GameContent.GOVT_CRISES[activeCrisisVal]
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1E36))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "EMERGENCY CABINET DIRECTIVE: ${activeCrisis.title}",
                                        color = Color.Yellow,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = activeCrisis.description, style = MaterialTheme.typography.bodySmall, color = Color.White)
                                    
                                    Spacer(modifier = Modifier.height(16.dp))

                                    activeCrisis.options.forEachIndexed { optKey, optVal ->
                                        Button(
                                            onClick = {
                                                onResolveCrisis(activeCrisisVal, optKey)
                                                activeCrisisVal = (activeCrisisVal + 1) % com.example.data.model.GameContent.GOVT_CRISES.size
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF101223))
                                        ) {
                                            Column {
                                                Text(optVal.text, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SaffronPrimary)
                                                Text(optVal.effectText, fontSize = 10.sp, color = Color(0xFF94A3B8))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- HOLD MEGA RALLY MODAL SHEET ---
            if (showRallySheet && focusedState != null) {
                var speechAsp by remember { mutableStateOf("We will bring rapid development and boost youth startups immediately!") }
                var speechAcc by remember { mutableStateOf("The opponent alliances have plundered central coffers for decades!") }
                var speechAss by remember { mutableStateOf("Every village will have 24/7 solar electric grids guaranteed!") }
                var selectedSecurity by remember { mutableStateOf("Standard") }

                AlertDialog(
                    onDismissRequest = { showRallySheet = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                onHoldRally(focusedState!!.stateName, selectedSecurity, speechAsp, speechAcc, speechAss)
                                showRallySheet = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                        ) {
                            Text("Commence Speech Call", color = Color.Black)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showRallySheet = false }) { Text("Cancel", color = Color.White) }
                    },
                    containerColor = Color(0xFF121422),
                    title = { Text("Draft Stadium Mega Rally Speech", color = SaffronPrimary) },
                    text = {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            Text("Rally security & hospitality:", fontSize = 11.sp, color = Color.White)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Standard", "Premium", "VIP").forEach { level ->
                                    val act = selectedSecurity == level
                                    Text(
                                        text = level,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (act) SaffronPrimary else Color.White,
                                        modifier = Modifier
                                            .clickable { selectedSecurity = level }
                                            .background(if (act) Color.White.copy(alpha = 0.1f) else Color.Transparent)
                                            .padding(8.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = speechAsp,
                                onValueChange = { speechAsp = it },
                                label = { Text("Speech Aspiration Topic", color = Color.White) },
                                textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 11.sp)
                            )
                            OutlinedTextField(
                                value = speechAcc,
                                onValueChange = { speechAcc = it },
                                label = { Text("Accusation Point Against rivals", color = Color.White) },
                                textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 11.sp)
                            )
                            OutlinedTextField(
                                value = speechAss,
                                onValueChange = { speechAss = it },
                                label = { Text("Assurance Guarantee Promise", color = Color.White) },
                                textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 11.sp)
                            )
                        }
                    }
                )
            }

            // --- WEB SOCIAL BLITZ SHEET ---
            if (showSocialSheet && focusedState != null) {
                var socialTopic by remember { mutableStateOf("VikasModel") }

                AlertDialog(
                    onDismissRequest = { showSocialSheet = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                onSocialBlitz(focusedState!!.stateName, socialTopic)
                                showSocialSheet = false
                            }
                        ) { Text("Blast Trend") }
                    },
                    dismissButton = { TextButton(onClick = { showSocialSheet = false }) { Text("Cancel") } },
                    containerColor = Color(0xFF1B1E36),
                    title = { Text("Launch Digital Hashtag Storm", color = SaffronPrimary) },
                    text = {
                        Column {
                            Text("Hashtag subject suffix:", fontSize = 11.sp, color = Color.White)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                listOf("DigitalIndia", "FarmersPride", "NariShakti", "YuvaFuture").forEach { topic ->
                                    Text(
                                        text = topic,
                                        fontSize = 11.sp,
                                        color = if (socialTopic == topic) SaffronPrimary else Color.White,
                                        modifier = Modifier
                                            .clickable { socialTopic = topic }
                                            .padding(6.dp)
                                    )
                                }
                            }
                        }
                    }
                )
            }

            // --- WELFARE SHEET ---
            if (showWelfareSheet && focusedState != null) {
                var selectedWelfare by remember { mutableStateOf("Farmers Direct Payout") }

                AlertDialog(
                    onDismissRequest = { showWelfareSheet = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                onPromiseWelfare(focusedState!!.stateName, selectedWelfare)
                                showWelfareSheet = false
                            }
                        ) { Text("File Welfare Promise") }
                    },
                    dismissButton = { TextButton(onClick = { showWelfareSheet = false }) { Text("Cancel") } },
                    containerColor = Color(0xFF1B1E36),
                    title = { Text("Launch Strategic Welfare Grant Promise", color = SaffronPrimary) },
                    text = {
                        Column {
                            listOf("Farmers Direct Payout", "Free Wifi & Laptops", "Pension & Ration Support").forEach { welfare ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedWelfare = welfare }
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(selected = selectedWelfare == welfare, onClick = { selectedWelfare = welfare })
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(welfare, color = Color.White)
                                }
                            }
                        }
                    }
                )
            }

            // --- TV DEBATE STUDIO ---
            if (showDebateSheet) {
                val questions = com.example.data.model.GameContent.TV_DEBATE_QUESTIONS
                var activeQKey by remember { mutableStateOf(0) }
                val currentQuestion = questions[activeQKey]

                AlertDialog(
                    onDismissRequest = { showDebateSheet = false },
                    confirmButton = {},
                    containerColor = Color(0xFF0F111E),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Tv, contentDescription = null, tint = SaffronPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("LIVE STUDIO TV CONFRONTATION", color = SaffronPrimary)
                        }
                    },
                    text = {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            Text(
                                "STUDIO QUESTION:",
                                fontWeight = FontWeight.Bold,
                                color = Color.Yellow,
                                fontSize = 12.sp
                            )
                            Text(
                                text = currentQuestion.question,
                                fontSize = 14.sp,
                                color = Color.White,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            currentQuestion.options.forEach { opt ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            onResolveDebate(opt)
                                            activeQKey = (activeQKey + 1) % questions.size
                                            showDebateSheet = false
                                        },
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1E36))
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(opt.text, fontWeight = FontWeight.Bold, color = SaffronPrimary, fontSize = 12.sp)
                                        Text(opt.summary, fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
                                    }
                                }
                            }
                        }
                    }
                )
            }

            // --- ADVISER PORTFOLIO DIALOG ---
            if (showAdvisoryDialog) {
                AlertDialog(
                    onDismissRequest = { showAdvisoryDialog = false },
                    confirmButton = {
                        Button(onClick = { showAdvisoryDialog = false }) { Text("Dismiss") }
                    },
                    containerColor = Color(0xFF1B1E36),
                    title = { Text("Constitutional Manifesto Guidelines", color = SaffronPrimary) },
                    text = {
                        Column {
                            Text("Ideology: ${game.ideology}", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Slogan: ${game.slogan}", fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.8f))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Policy Focus: ${game.manifestoFocus}", fontWeight = FontWeight.SemiBold, color = EmeraldGreen)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Advice Tracker:\nHold rallies during Sunny days to maximize turnout safely without waterproofing costs. Complete web social trends when funds are low to gain support free!", fontSize = 12.sp, color = Color.White)
                        }
                    }
                )
            }

            // --- CINEMATIC GEMINI ADVISOR OVERLAY ---
            if (isAILoading || aiSpeechResponse != null) {
                AlertDialog(
                    onDismissRequest = { if (!isAILoading) onClearAI() },
                    confirmButton = {
                        if (!isAILoading) {
                            Button(onClick = onClearAI, colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)) {
                                Text("Acknowledge Broadcast", color = Color.Black)
                            }
                        }
                    },
                    containerColor = Color(0xFF0C0E1B),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LiveTv, contentDescription = null, tint = Color.Red)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("BHARAT LIVE ELECTION FEED", color = SaffronPrimary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                        }
                    },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                        ) {
                            if (isAILoading) {
                                CircularProgressIndicator(color = SaffronPrimary)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("TRANSMITTING FROM POLITICAL ANALYSTS...", color = Color.White, fontSize = 11.sp)
                            } else {
                                Text(
                                    text = aiSpeechResponse ?: "",
                                    fontSize = 13.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.testTag("stat_${title.lowercase().replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1E36))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
            Text(title, fontSize = 9.sp, color = Color.White.copy(alpha = 0.6f))
        }
    }
}
