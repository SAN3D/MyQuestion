package com.questionpaper.repository.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.SwipeRefresh
import androidx.compose.material3.rememberSwipeRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.questionpaper.repository.data.QuestionPaper
import com.questionpaper.repository.viewmodels.MainViewModel

@Composable
fun QuestionPaperList(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val papers by viewModel.questionPapers.collectAsState()

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(papers) { paper ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = paper.subject,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Uploaded: ${paper.uploadDate}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Button(
                        onClick = { 
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(Uri.fromFile(File(paper.filePath)), "application/pdf")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            LocalContext.current.startActivity(intent)
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("View Paper")
                    }
                }
            }
        }
    }
}