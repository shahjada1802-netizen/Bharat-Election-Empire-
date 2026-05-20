package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.StateProgress
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.SaffronPrimary
import kotlin.math.roundToInt

@Composable
fun IndiaMapRenderer(
    states: List<StateProgress>,
    selectedStateName: String?,
    onStateSelect: (StateProgress) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .testTag("india_tactical_map_card"),
        shape = RoundedCornerShape(24.dp), // Rounded-[2rem] style
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color(0xFFFFEDD5)) // Orange-100 border accent
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = "Map Symbol",
                        tint = SaffronPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Bharat Campaign Command Center",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground // Replaced hardcoded Color.White
                    )
                }
                Badge(
                    containerColor = EmeraldGreen,
                    contentColor = Color.White
                ) {
                    Text("12 Major States Active", modifier = Modifier.padding(4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Text(
                text = "Tap any state sector below to view local support rates, weather alerts, and coordinate rallies.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Map visualization viewport matching HTML `.bg-slate-900 rounded-[2.5rem]`
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFF0F172A), RoundedCornerShape(20.dp)) // Slate-900 style
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background futuristic grid lines
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    
                    // Draw grid coordinates
                    val gridDensity = 10
                    for (i in 0..gridDensity) {
                        val dx = (w / gridDensity) * i
                        val dy = (h / gridDensity) * i
                        drawLine(
                            color = Color.White.copy(alpha = 0.05f),
                            start = Offset(dx, 0f),
                            end = Offset(dx, h),
                            strokeWidth = 1f
                        )
                        drawLine(
                            color = Color.White.copy(alpha = 0.05f),
                            start = Offset(0f, dy),
                            end = Offset(w, dy),
                            strokeWidth = 1f
                        )
                    }

                    // Key structural hubs (Delhi, UP, Karnataka, WB, Maharashtra)
                    val delhiPos = Offset(w * 0.4f, h * 0.25f)
                    val upPos = Offset(w * 0.52f, h * 0.35f)
                    val biharPos = Offset(w * 0.68f, h * 0.38f)
                    val wbPos = Offset(w * 0.76f, h * 0.48f)
                    val gujPos = Offset(w * 0.2f, h * 0.52f)
                    val mahaPos = Offset(w * 0.35f, h * 0.62f)
                    val karnatakaPos = Offset(w * 0.38f, h * 0.78f)
                    val tnPos = Offset(w * 0.45f, h * 0.9f)
                    val keralaPos = Offset(w * 0.35f, h * 0.88f)
                    val apPos = Offset(w * 0.52f, h * 0.72f)
                    val mpPos = Offset(w * 0.45f, h * 0.48f)
                    val rajPos = Offset(w * 0.28f, h * 0.38f)

                    // Draw connection lines
                    val hubs = listOf(
                        delhiPos to upPos, upPos to biharPos, biharPos to wbPos,
                        delhiPos to rajPos, rajPos to gujPos, gujPos to mahaPos,
                        mahaPos to karnatakaPos, karnatakaPos to keralaPos,
                        karnatakaPos to tnPos, tnPos to apPos, apPos to mpPos,
                        mpPos to upPos, mpPos to mahaPos
                    )

                    hubs.forEach { (start, end) ->
                        drawLine(
                            color = SaffronPrimary.copy(alpha = 0.35f),
                            start = start,
                            end = end,
                            strokeWidth = 2.dp.toPx()
                        )
                    }

                    // Draw glow pulses around key player lead states
                    drawCircle(
                        color = SaffronPrimary.copy(alpha = 0.2f),
                        radius = 35f,
                        center = delhiPos
                    )
                }

                // Tactile floating nodes over canvas
                // Mapping absolute state coordinates on our canvas screen representation
                val positionalStates = listOf(
                    "Delhi" to (0.4f to 0.22f),
                    "Uttar Pradesh" to (0.52f to 0.33f),
                    "Bihar" to (0.68f to 0.38f),
                    "West Bengal" to (0.76f to 0.48f),
                    "Gujarat" to (0.2f to 0.52f),
                    "Maharashtra" to (0.35f to 0.62f),
                    "Karnataka" to (0.38f to 0.78f),
                    "Tamil Nadu" to (0.45f to 0.9f),
                    "Kerala" to (0.32f to 0.86f),
                    "Andhra Pradesh" to (0.52f to 0.72f),
                    "Madhya Pradesh" to (0.45f to 0.48f),
                    "Rajasthan" to (0.28f to 0.38f)
                )

                positionalStates.forEach { (name, coords) ->
                    val stateProg = states.find { it.stateName == name }
                    if (stateProg != null) {
                        val isSelected = selectedStateName == name
                        val supportColor = when {
                            stateProg.playerSupportRate > 40.0 -> EmeraldGreen
                            stateProg.playerSupportRate > 20.0 -> SaffronPrimary
                            else -> Color(0xFF64748B)
                        }

                        Box(
                            modifier = Modifier
                                .align(BiasAlignment(coords.first * 2f - 1f, coords.second * 2f - 1f))
                                .size(if (isSelected) 24.dp else 16.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(supportColor, Color.Transparent),
                                    ),
                                )
                                .clickable { onStateSelect(stateProg) },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        if (isSelected) Color.White else supportColor,
                                        RoundedCornerShape(50)
                                    )
                            )
                        }
                    }
                }

                // North/South indicators
                Text(
                    text = "N",
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    color = SaffronPrimary,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(4.dp)
                )
                Text(
                    text = "S",
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    color = EmeraldGreen,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Display short summary of selected state
            selectedStateName?.let { name ->
                val selectedProgress = states.find { it.stateName == name }
                selectedProgress?.let { prog ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SaffronPrimary.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFFFEDD5), RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = prog.stateName,
                                fontWeight = FontWeight.Black,
                                color = SaffronPrimary,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Local focus: ${prog.mainIssue} | Weather: ${prog.weather}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Your Votes: ${prog.playerSupportRate.roundToInt()}%",
                                fontWeight = FontWeight.Black,
                                color = EmeraldGreen,
                                fontSize = 14.sp
                            )
                            val statusText = when {
                                prog.playerSupportRate > 50.0 -> "Major Lead"
                                prog.playerSupportRate > prog.oppositionSupportRate -> "Advancing"
                                else -> "Needs Camp."
                            }
                            Text(
                                text = statusText,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } ?: Box {
                Text(
                    text = "👈 Tap any node to focus state info",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
