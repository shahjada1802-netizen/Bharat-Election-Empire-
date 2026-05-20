package com.example.data.repository

import com.example.data.dao.ElectionGameDao
import com.example.data.entity.GameSave
import com.example.data.entity.StateProgress
import com.example.data.entity.ElectionLog
import kotlinx.coroutines.flow.Flow

class ElectionRepository(private val dao: ElectionGameDao) {

    val gameSave: Flow<GameSave?> = dao.getGameSaveFlow()
    val stateProgresses: Flow<List<StateProgress>> = dao.getAllStateProgress()
    val logs: Flow<List<ElectionLog>> = dao.getAllElectionLogs()

    suspend fun getGameSaveSync(): GameSave? = dao.getGameSaveSync()

    suspend fun saveGame(gameSave: GameSave) = dao.saveGame(gameSave)

    suspend fun deleteGame(): Unit {
        dao.deleteGameSave()
        dao.clearStateProgress()
        dao.clearLogs()
    }

    suspend fun getStateProgressSync(stateName: String): StateProgress? = dao.getStateProgressSync(stateName)

    suspend fun insertStateProgress(stateProgress: StateProgress) = dao.insertStateProgress(stateProgress)

    suspend fun insertStateProgressList(list: List<StateProgress>) = dao.insertStateProgressList(list)

    suspend fun insertLog(log: ElectionLog) = dao.insertLog(log)

    suspend fun clearAll() {
        dao.deleteGameSave()
        dao.clearStateProgress()
        dao.clearLogs()
    }
}
