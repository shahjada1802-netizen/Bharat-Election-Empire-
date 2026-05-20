package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.GameSave
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.SaffronPrimary
import kotlin.math.roundToInt

@Composable
fun ResultsScreen(
    game: GameSave,
    progress: Float,
    playerSeatsCount: Int,
    opponentsSeatsCount: Int,
    allianceSeatsCount: Int,
    tickerMessage: String,
    completed: Boolean,
    success: Boolean,
    onCloseResults: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F111E))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("results_screen_column"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // News Studio Visuals Badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.Red, RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(Icons.Default.LiveTv, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("ELECTION RESULTS LIVE UNIT COUNTING", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "ELECTION MANDATE DECISION",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = SaffronPrimary,
            letterSpacing = 1.sp
        )
        Text(
            text = "Career Level Target: ${game.careerStage} Elections",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Progress bar
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .testTag("counting_progress_bar"),
            color = SaffronPrimary,
            trackColor = Color.White.copy(alpha = 0.1f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Ticker Message Alert Panel
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1E36))
        ) {
            Text(
                text = tickerMessage,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Seat metrics layout depending on career stage
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF101223))
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                if (game.careerStage == "MLA") {
                    Text("Starting Constituency Poll Votes", fontWeight = FontWeight.Bold, color = SaffronPrimary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("YOUR PARTY %", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                            Text("${(progress * game.mlaVotesReceived / 1000f).roundToInt()}k Estim.", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("OPPONENTS %", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                            Text("${(progress * game.mlaOpponentVotes / 1000f).roundToInt()}k Estim.", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                        }
                    }
                } else {
                    val targetToWin = if (game.careerStage == "CM") game.cmTotalSeatsInState / 2 + 1 else 272
                    val denominator = if (game.careerStage == "CM") game.cmTotalSeatsInState else 543
                    
                    Text("TOTAL PARLIAMENT SECTORS DECLARED: $denominator", fontWeight = FontWeight.Bold, color = SaffronPrimary, fontSize = 11.sp)
                    Text("Target Majority Threshold: $targetToWin", fontSize = 10.sp, color = Color.Yellow)
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("YOUR SEATS", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                            Text("$playerSeatsCount", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldGreen)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("RIVAL BLOCK", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                            Text("$opponentsSeatsCount", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.Red)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("IND/ALLIANCE", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                            Text("$allianceSeatsCount", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Actions at the end of counting
        if (completed) {
            if (success) {
                Icon(Icons.Default.Celebration, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(54.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "CONGRATULATIONS LEADER!\nYou have won the democrat mandate. Moving to the executive chamber of government management.",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = EmeraldGreen,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onCloseResults,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("results_proceed_btn")
                ) {
                    Text("CLAIM OFFICE DIRECTIVE & ADVANCE", fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
            } else {
                Icon(Icons.Default.Dangerous, contentDescription = null, tint = Color.Red, modifier = Modifier.size(54.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "ELECTION FAILED TO SECURE MAJORITY\nThe public mandate went in favor of rival coalition partners. Go back to rally campaigns and redesign manifestos.",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onCloseResults,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("results_proceed_btn")
                ) {
                    Text("RETRY CAMPAIGN STRATEGY", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(color = SaffronPrimary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("EVM units processing...", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
    }
}
