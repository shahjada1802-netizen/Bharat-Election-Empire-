package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_saves")
data class GameSave(
    @PrimaryKey val id: Int = 1, // Only 1 active slot
    val playerName: String,
    val playerAge: Int,
    val playerState: String,
    val characterStyle: Int, // Costume index
    val partyName: String,
    val partySymbol: String,
    val partyColor: String, // Hex string
    val ideology: String,
    val slogan: String,
    val manifestoFocus: String,
    
    // Core RPG player stats
    val funds: Double, // in Lakhs / Crores
    val fame: Int, // 1 - 100
    val charisma: Int, // 1 - 10
    val speakingSkill: Int, // 1 - 10
    val socialMediaPopularity: Int, // 1 - 100
    val corruptionScandalIndex: Int, // 0 - 100
    
    // Campaign & Game progress
    val currentTurn: Int,
    val currentYear: Int,
    val careerStage: String, // "MLA", "CM", "PM"
    val isGameOver: Boolean = false,
    val activeElectionStateName: String? = null, // Current targeted state if any
    
    // MLA level stats
    val mlaAssemblyWon: Boolean = false,
    val mlaVotesReceived: Int = 0,
    val mlaOpponentVotes: Int = 0,
    
    // CM level stats
    val cmSeatsWon: Int = 0,
    val cmTotalSeatsInState: Int = 0,
    val cmCoalitionFormed: Boolean = false,
    
    // PM level stats
    val pmSeatsWon: Int = 0,
    val pmTotalSeats: Int = 0,
    val pmCoalitionFormed: Boolean = false,
    
    // Government Mode Stats (After winning)
    val isInGovtMode: Boolean = false,
    val approvalRating: Int = 50, // 0 - 100
    val regionalDevelopmentIndex: Int = 50, // 0 - 100
    val inflationRate: Double = 5.5, // %
    val jobsCreatedCount: Int = 0 // in thousands
)
