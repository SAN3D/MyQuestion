package com.questionpaper.repository.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dhaval2404.imagepicker.ImagePicker
import com.questionpaper.repository.R
import com.questionpaper.repository.viewmodels.MainViewModel
import java.io.File

@Composable
fun FileUploadSection(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedFile by remember { mutableStateOf<File?>(null) }
    var subject by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ImagePicker.getContract(),
        onResult = { file -> 
            file?.let { selectedFile = it }
        }
    )

    Column(modifier = modifier) {
        // Subject Input
        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Subject") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // File Picker Button
        Button(
            onClick = { 
                ImagePicker.with(context)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent { intent ->
                        launcher.launch(intent)
                    }
            }
        ) {
            Text(stringResource(R.string.select_file))
        }

        Spacer(Modifier.height(8.dp))

        // Selected File Info
        selectedFile?.let { file ->
            val isPdf = file.extension.equals("pdf", ignoreCase = true)
            val fileSizeMB = file.length() / (1024.0 * 1024.0)
            val isValidSize = fileSizeMB <= 5
            val isValidFile = isPdf && isValidSize
            
            Column {
                Text(
                    text = "Selected: ${file.name} (${"%.2f".format(fileSizeMB)} MB)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (!isValidFile) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
                
                if (!isPdf) {
                    Text(
                        text = "Only PDF files are allowed",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                if (!isValidSize) {
                    Text(
                        text = "File size must be under 5MB",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Upload Button
            val uploadStatus by viewModel.uploadStatus.collectAsStateWithLifecycle()
            
            Column {
                when (val status = uploadStatus) {
                    is MainViewModel.UploadStatus.InProgress -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator()
                            Spacer(Modifier.height(8.dp))
                            Text("Uploading...")
                        }
                    }
                    is MainViewModel.UploadStatus.Success -> {
                        AlertDialog(
                            onDismissRequest = { viewModel.resetUploadStatus() },
                            title = { Text("Success") },
                            text = { Text(status.message) },
                            confirmButton = {
                                Button(onClick = { viewModel.resetUploadStatus() }) {
                                    Text("OK")
                                }
                            }
                        )
                        // Reset form after successful upload
                        LaunchedEffect(status) {
                            selectedFile = null
                            subject = ""
                        }
                    }
                    is MainViewModel.UploadStatus.Error -> {
                        AlertDialog(
                            onDismissRequest = { viewModel.resetUploadStatus() },
                            title = { Text("Error") },
                            text = { Text(status.message) },
                            confirmButton = {
                                Button(onClick = { viewModel.resetUploadStatus() }) {
                                    Text("OK")
                                }
                            }
                        )
                    }
                    else -> {
                        Button(
                            onClick = { 
                                viewModel.uploadQuestionPaper(
                                    filePath = file.absolutePath,
                                    subject = subject
                                )
                            },
                            enabled = viewModel.selectedFaculty.value != null &&
                                    viewModel.selectedSemester.value != null &&
                                    viewModel.selectedYear.value != null &&
                                    subject.isNotBlank() &&
                                    selectedFile != null &&
                                    selectedFile?.extension.equals("pdf", ignoreCase = true) == true &&
                                    (selectedFile?.length() ?: 0) <= 5 * 1024 * 1024,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.upload_button))
                        }
                    }
                }
            }
        }
    }
}