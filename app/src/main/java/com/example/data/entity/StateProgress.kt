package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "state_progress")
data class StateProgress(
    @PrimaryKey val stateName: String, // e.g. Uttar Pradesh, Maharashtra
    val assemblySeats: Int,
    val lokSabhaSeats: Int,
    val ruralPercent: Int,
    val urbanPercent: Int,
    
    // Core state requirements
    val mainIssue: String, // "Agriculture", "Technology", "Welfare", "Unemployment", "Inflation", "Education"
    
    // Player progress in this state
    val playerSupportRate: Double, // 0.0 - 100.0 (Our vote percentage share)
    val oppositionSupportRate: Double, // 0.0 - 100.0 (Opposition base)
    val undecidedSupportRate: Double, // 0.0 - 100.0 (Swing voters)
    
    val ralliesHeld: Int,
    val advertisingSpent: Double, // Lakhs
    val welfareSchemesPromised: Int,
    
    // Local environment factors
    val weather: String, // "Sunny", "Monsoon", "Heatwave", "Cyclone"
    val regionalAtmosphere: String // "Festival season", "Agrarian distress", "Economic boom", "Protests"
)
