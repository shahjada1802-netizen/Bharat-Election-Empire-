package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "election_logs")
data class ElectionLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val tag: String, // "CAMPAIGN", "NEWS", "MILESTONE", "SCANDAL"
    val title: String,
    val details: String,
    val stateAffected: String? = null
)
