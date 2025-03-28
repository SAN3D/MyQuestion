package com.questionpaper.repository.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.questionpaper.repository.R
import com.questionpaper.repository.model.SelectionOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    var showUpload by remember { mutableStateOf(true) }
    val faculties = listOf(
        SelectionOption(1, "Engineering"),
        SelectionOption(2, "Medicine"),
        SelectionOption(3, "Science")
    )
    
    LaunchedEffect(viewModel.selectedFaculty.value, viewModel.selectedSemester.value, viewModel.selectedYear.value) {
        if (!showUpload) {
            viewModel.fetchQuestionPapers()
        }
    }
    val semesters = (1..8).map { SelectionOption(it, "Semester $it") }
    val years = (2018..2023).map { SelectionOption(it, it.toString()) }

    val selectedFaculty by viewModel.selectedFaculty.collectAsState()
    val selectedSemester by viewModel.selectedSemester.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.welcome_message),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Faculty Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedFaculty,
                onExpandedChange = { expandedFaculty = !expandedFaculty }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedFaculty?.displayName ?: stringResource(R.string.select_faculty),
                    onValueChange = {},
                    label = { Text(stringResource(R.string.select_faculty)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFaculty) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedFaculty,
                    onDismissRequest = { expandedFaculty = false }
                ) {
                    faculties.forEach { faculty ->
                        DropdownMenuItem(
                            text = { Text(faculty.displayName) },
                            onClick = {
                                selectedFaculty = faculty
                                expandedFaculty = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Semester Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedSemester,
                onExpandedChange = { expandedSemester = !expandedSemester }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedSemester?.displayName ?: stringResource(R.string.select_semester),
                    onValueChange = {},
                    label = { Text(stringResource(R.string.select_semester)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSemester) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedSemester,
                    onDismissRequest = { expandedSemester = false }
                ) {
                    semesters.forEach { semester ->
                        DropdownMenuItem(
                            text = { Text(semester.displayName) },
                            onClick = {
                                selectedSemester = semester
                                expandedSemester = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Year Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedYear,
                onExpandedChange = { expandedYear = !expandedYear }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedYear?.displayName ?: stringResource(R.string.select_year),
                    onValueChange = {},
                    label = { Text(stringResource(R.string.select_year)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedYear) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedYear,
                    onDismissRequest = { expandedYear = false }
                ) {
                    years.forEach { year ->
                        DropdownMenuItem(
                            text = { Text(year.displayName) },
                            onClick = {
                                selectedYear = year
                                expandedYear = false
                            }
                        )
                    }
                }
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { showUpload = !showUpload },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (showUpload) "View Papers" else "Upload Papers")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (showUpload) {
                FileUploadSection(viewModel = viewModel)
            } else {
                QuestionPaperList(viewModel = viewModel)
            }
        }
    }
}
