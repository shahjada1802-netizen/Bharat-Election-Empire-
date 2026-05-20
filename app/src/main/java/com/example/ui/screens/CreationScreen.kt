package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.GoldSteel

@Composable
fun CreationScreen(
    onGameStart: (
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
    ) -> Unit
) {
    // Character variables
    var playerName by remember { mutableStateOf("Rajesh Kumar") }
    var playerAge by remember { mutableStateOf("29") }
    var startingState by remember { mutableStateOf("Uttar Pradesh") }
    var costumeIndex by remember { mutableStateOf(0) }

    // RPG attributes (15 skill points to split!)
    var pointsLeft by remember { mutableStateOf(15) }
    var charisma by remember { mutableStateOf(5) }
    var speakingSkill by remember { mutableStateOf(5) }
    var digitalReach by remember { mutableStateOf(5) }

    // Party variables
    var partyName by remember { mutableStateOf("Lok Pragati Dal") }
    var selectedSymbol by remember { mutableStateOf("Lotus") }
    var selectedColor by remember { mutableStateOf("#FF9933") } // Saffron hex
    var selectedIdeology by remember { mutableStateOf("Startup & Tech") }
    var customSlogan by remember { mutableStateOf("Vikas aur Yuva Shakti!") }
    var manifestoFocus by remember { mutableStateOf("Youth Employment") }

    val symbols = listOf("Lotus", "Hand", "Elephant", "Arrow", "Broom", "Rising Sun", "Cycle", "Lion")
    val ideologies = listOf("Startup & Tech", "Socialist", "Nationalist", "Rural development", "Environmental", "Youth-focused")
    val states = listOf("Uttar Pradesh", "Maharashtra", "West Bengal", "Bihar", "Tamil Nadu", "Karnataka", "Gujarat", "Rajasthan", "Delhi")
    val manifestos = listOf("Youth Employment", "Rural Agriculture Infrastructure", "Tech & Startup Capitals", "Universal Welfare Subsidies", "Healthcare & High Literacy")

    // OutlinedTextField styles corresponding to Tailwind "bg-orange-50 border-orange-100" config
    val vibrantTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
        focusedContainerColor = Color(0xFFFFF7ED), // Orange-50 (very soft light orange accent tint)
        unfocusedContainerColor = Color(0xFFFFF7ED),
        focusedBorderColor = SaffronPrimary,
        unfocusedBorderColor = Color(0xFFFFEDD5), // Orange-100
        focusedLabelColor = SaffronPrimary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Peach warm base background
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("creation_screen_column"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Dramatic header
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "BHARAT ELECTION EMPIRE",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = Color(0xFF0F172A), // Slate-900 (Stately Title Accent)
            letterSpacing = 1.5.sp
        )
        Text(
            text = "Create Your Leadership Profile & Rise to Power",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // --- Section 1: Candidate Profile ---
        Text(
            text = "1. Candidate Specifications",
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            fontWeight = FontWeight.Black,
            color = SaffronPrimary,
            fontSize = 18.sp
        )

        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp), // Rounded-[2rem] scale
            border = BorderStroke(1.dp, Color(0xFFFFEDD5)) // Orange-100 soft border accent
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = playerName,
                    onValueChange = { playerName = it },
                    label = { Text("Candidate Full Name") },
                    colors = vibrantTextFieldColors,
                    modifier = Modifier.fillMaxWidth().testTag("candidate_name_tf")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = playerAge,
                        onValueChange = { if (it.length <= 3) playerAge = it },
                        label = { Text("Age (Min 25)") },
                        colors = vibrantTextFieldColors,
                        modifier = Modifier.weight(1f).testTag("candidate_age_tf")
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Starting State",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(Color(0xFFFFF7ED), RoundedCornerShape(4.dp))
                                .border(1.dp, Color(0xFFFFEDD5), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = startingState,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scroll starting state mini-selector
                Text(
                    text = "Select Starting constituency base:",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .background(Color(0xFFFFF7ED), RoundedCornerShape(8.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    states.take(4).forEach { st ->
                        val active = startingState == st
                        Text(
                            text = st.take(10),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (active) SaffronPrimary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            modifier = Modifier
                                .clickable { startingState = st }
                                .background(if (active) SaffronPrimary.copy(alpha = 0.15f) else Color.Transparent, RoundedCornerShape(4.dp))
                                .padding(6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // RPG Stats splits
                Text(
                    text = "Skillpoints Pool: $pointsLeft Left",
                    fontWeight = FontWeight.Bold,
                    color = GoldSteel, // Rich Amber/Gold
                    fontSize = 14.sp
                )

                // Charisma stat
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Charisma Index: $charisma",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1.5f)
                    )
                    Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                        Button(
                            onClick = { if (charisma > 1) { charisma--; pointsLeft++ } },
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary, contentColor = Color.White),
                            modifier = Modifier.size(36.dp)
                        ) { Text("-", fontWeight = FontWeight.Bold) }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { if (pointsLeft > 0) { charisma++; pointsLeft-- } },
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary, contentColor = Color.White),
                            modifier = Modifier.size(36.dp)
                        ) { Text("+", fontWeight = FontWeight.Bold) }
                    }
                }

                // Public speaking
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Oratory Skill: $speakingSkill",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1.5f)
                    )
                    Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                        Button(
                            onClick = { if (speakingSkill > 1) { speakingSkill--; pointsLeft++ } },
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary, contentColor = Color.White),
                            modifier = Modifier.size(36.dp)
                        ) { Text("-", fontWeight = FontWeight.Bold) }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { if (pointsLeft > 0) { speakingSkill++; pointsLeft-- } },
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary, contentColor = Color.White),
                            modifier = Modifier.size(36.dp)
                        ) { Text("+", fontWeight = FontWeight.Bold) }
                    }
                }

                // Digital Popularity
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Digital Popularity: $digitalReach",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1.5f)
                    )
                    Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                        Button(
                            onClick = { if (digitalReach > 1) { digitalReach--; pointsLeft++ } },
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary, contentColor = Color.White),
                            modifier = Modifier.size(36.dp)
                        ) { Text("-", fontWeight = FontWeight.Bold) }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { if (pointsLeft > 0) { digitalReach++; pointsLeft-- } },
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary, contentColor = Color.White),
                            modifier = Modifier.size(36.dp)
                        ) { Text("+", fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }

        // --- Section 2: Party Creation ---
        Text(
            text = "2. Party & Ideology Builder",
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            fontWeight = FontWeight.Black,
            color = SaffronPrimary,
            fontSize = 18.sp
        )

        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color(0xFFFFEDD5))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = partyName,
                    onValueChange = { partyName = it },
                    label = { Text("Political Party Name") },
                    colors = vibrantTextFieldColors,
                    modifier = Modifier.fillMaxWidth().testTag("party_name_tf")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = customSlogan,
                    onValueChange = { customSlogan = it },
                    label = { Text("Campaign Slogan / Mantra") },
                    colors = vibrantTextFieldColors,
                    modifier = Modifier.fillMaxWidth().testTag("slogan_tf")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Party Symbol Grid
                Text(
                    "Select Representative Symbol",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .background(Color(0xFFFFF7ED), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    symbols.take(4).forEach { sym ->
                        val active = selectedSymbol == sym
                        Text(
                            text = sym,
                            color = if (active) SaffronPrimary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .clickable { selectedSymbol = sym }
                                .background(if (active) SaffronPrimary.copy(alpha = 0.15f) else Color.Transparent, RoundedCornerShape(4.dp))
                                .padding(6.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 8.dp)
                        .background(Color(0xFFFFF7ED), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    symbols.drop(4).forEach { sym ->
                        val active = selectedSymbol == sym
                        Text(
                            text = sym,
                            color = if (active) SaffronPrimary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .clickable { selectedSymbol = sym }
                                .background(if (active) SaffronPrimary.copy(alpha = 0.15f) else Color.Transparent, RoundedCornerShape(4.dp))
                                .padding(6.dp)
                        )
                    }
                }

                // Party Ideology Scroll
                Text(
                    "Primary Political Ideology",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    ideologies.chunked(3).forEach { chunk ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            chunk.forEach { ideo ->
                                val active = selectedIdeology == ideo
                                Text(
                                    text = ideo,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (active) Color.White else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedIdeology = ideo }
                                        .background(if (active) EmeraldGreen else Color(0xFFFFF7ED), RoundedCornerShape(4.dp))
                                        .padding(8.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Manifesto Selection Focus
                Text(
                    "Manifesto Flagship Focus",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    manifestos.forEach { manify ->
                        val isSelected = manifestoFocus == manify
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .background(if (isSelected) SaffronPrimary.copy(alpha = 0.12f) else Color(0xFFFFF7ED), RoundedCornerShape(4.dp))
                                .clickable { manifestoFocus = manify }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = manify,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // Action submit button
        Button(
            onClick = {
                val validatedAge = playerAge.toIntOrNull() ?: 25
                onGameStart(
                    playerName,
                    if (validatedAge < 25) 25 else validatedAge,
                    startingState,
                    partyName,
                    selectedSymbol,
                    selectedColor,
                    selectedIdeology,
                    customSlogan,
                    manifestoFocus,
                    costumeIndex
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary, contentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("comence_campaign_btn"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "COMMENCE BHARAT CAMPAIGN",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(48.dp))
    }
}
