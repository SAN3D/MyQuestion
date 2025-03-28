package com.questionpaper.repository.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.questionpaper.repository.data.QuestionPaperRepository
import com.questionpaper.repository.model.SelectionOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: QuestionPaperRepository
) : ViewModel() {
    private val _selectedFaculty = MutableStateFlow<SelectionOption?>(null)
    val selectedFaculty: StateFlow<SelectionOption?> = _selectedFaculty.asStateFlow()

    private val _selectedSemester = MutableStateFlow<SelectionOption?>(null)
    val selectedSemester: StateFlow<SelectionOption?> = _selectedSemester.asStateFlow()

    private val _selectedYear = MutableStateFlow<SelectionOption?>(null)
    val selectedYear: StateFlow<SelectionOption?> = _selectedYear.asStateFlow()

    fun selectFaculty(faculty: SelectionOption) {
        _selectedFaculty.value = faculty
    }

    fun selectSemester(semester: SelectionOption) {
        _selectedSemester.value = semester
    }

    fun selectYear(year: SelectionOption) {
        _selectedYear.value = year
    }

    sealed class UploadStatus {
        object Idle : UploadStatus()
        object InProgress : UploadStatus()
        data class Success(val message: String) : UploadStatus()
        data class Error(val message: String) : UploadStatus()
    }

    private val _uploadStatus = MutableStateFlow<UploadStatus>(UploadStatus.Idle)
    val uploadStatus: StateFlow<UploadStatus> = _uploadStatus.asStateFlow()

    fun uploadQuestionPaper(filePath: String, subject: String) {
        viewModelScope.launch {
            _uploadStatus.value = UploadStatus.InProgress
            try {
                val facultyId = selectedFaculty.value?.id ?: throw Exception("Select faculty")
                val semester = selectedSemester.value?.id ?: throw Exception("Select semester")
                val year = selectedYear.value?.id ?: throw Exception("Select year")

                val questionPaper = QuestionPaper(
                    facultyId = facultyId,
                    semester = semester,
                    year = year,
                    subject = subject,
                    filePath = filePath
                )
                repository.addQuestionPaper(questionPaper)
                _uploadStatus.value = UploadStatus.Success("Upload successful!")
            } catch (e: Exception) {
                _uploadStatus.value = UploadStatus.Error(e.message ?: "Upload failed")
            }
        }
    }

    fun resetUploadStatus() {
        _uploadStatus.value = UploadStatus.Idle
    }

    private val _questionPapers = MutableStateFlow<List<QuestionPaper>>(emptyList())
    val questionPapers: StateFlow<List<QuestionPaper>> = _questionPapers.asStateFlow()

    fun fetchQuestionPapers() {
        viewModelScope.launch {
            try {
                val facultyId = selectedFaculty.value?.id ?: return@launch
                val semester = selectedSemester.value?.id ?: return@launch
                val year = selectedYear.value?.id ?: return@launch
                
                _questionPapers.value = repository.getQuestionPapers(facultyId, semester, year)
            } catch (e: Exception) {
                _uploadStatus.value = UploadStatus.Error("Failed to fetch papers")
            }
        }
    }
}
