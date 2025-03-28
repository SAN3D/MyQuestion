package com.questionpaper.repository.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

@Entity(tableName = "question_papers")
data class QuestionPaper(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val facultyId: Int,
    val semester: Int,
    val year: Int,
    val subject: String,
    val filePath: String,
    val uploadDate: Date = Date()
) {
    val formattedDate: String
        get() = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault()).format(uploadDate)
}
