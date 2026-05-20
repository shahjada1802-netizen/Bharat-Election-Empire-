package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.GameSave
import com.example.data.entity.StateProgress
import com.example.data.entity.ElectionLog
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionGameDao {

    @Query("SELECT * FROM game_saves WHERE id = 1 LIMIT 1")
    fun getGameSaveFlow(): Flow<GameSave?>

    @Query("SELECT * FROM game_saves WHERE id = 1 LIMIT 1")
    suspend fun getGameSaveSync(): GameSave?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGame(gameSave: GameSave)

    @Query("DELETE FROM game_saves")
    suspend fun deleteGameSave()

    @Query("SELECT * FROM state_progress")
    fun getAllStateProgress(): Flow<List<StateProgress>>

    @Query("SELECT * FROM state_progress WHERE stateName = :stateName LIMIT 1")
    suspend fun getStateProgressSync(stateName: String): StateProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStateProgress(stateProgress: StateProgress)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStateProgressList(list: List<StateProgress>)

    @Query("DELETE FROM state_progress")
    suspend fun clearStateProgress()

    @Query("SELECT * FROM election_logs ORDER BY timestamp DESC LIMIT 50")
    fun getAllElectionLogs(): Flow<List<ElectionLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ElectionLog)

    @Query("DELETE FROM election_logs")
    suspend fun clearLogs()
}
