package com.questionpaper.repository.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestionPaperDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionPaper(questionPaper: QuestionPaper)

    @Query("SELECT * FROM question_papers WHERE facultyId = :facultyId AND semester = :semester AND year = :year")
    suspend fun getQuestionPapers(facultyId: Int, semester: Int, year: Int): List<QuestionPaper>
}