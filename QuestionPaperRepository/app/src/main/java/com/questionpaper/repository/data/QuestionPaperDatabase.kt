package com.questionpaper.repository.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [QuestionPaper::class], version = 1, exportSchema = false)
abstract class QuestionPaperDatabase : RoomDatabase() {
    abstract fun questionPaperDao(): QuestionPaperDao

    companion object {
        @Volatile
        private var INSTANCE: QuestionPaperDatabase? = null

        fun getDatabase(context: Context): QuestionPaperDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuestionPaperDatabase::class.java,
                    "question_paper_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}