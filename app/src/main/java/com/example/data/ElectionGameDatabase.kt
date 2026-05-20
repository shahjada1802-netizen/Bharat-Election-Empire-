package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.ElectionGameDao
import com.example.data.entity.GameSave
import com.example.data.entity.StateProgress
import com.example.data.entity.ElectionLog

@Database(
    entities = [GameSave::class, StateProgress::class, ElectionLog::class],
    version = 1,
    exportSchema = false
)
abstract class ElectionGameDatabase : RoomDatabase() {
    abstract fun electionGameDao(): ElectionGameDao

    companion object {
        @Volatile
        private var INSTANCE: ElectionGameDatabase? = null

        fun getDatabase(context: Context): ElectionGameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ElectionGameDatabase::class.java,
                    "bharat_election_empire_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
