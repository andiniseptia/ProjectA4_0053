package com.example.pam_uas_andini.ui.view.jenis

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pam_uas_andini.R
import com.example.pam_uas_andini.customwidget.CostumeTopAppBar
import com.example.pam_uas_andini.model.Jenis
import com.example.pam_uas_andini.navigation.DestinasiDetailJenis
import com.example.pam_uas_andini.ui.viewmodel.PenyediaViewModel
import com.example.pam_uas_andini.ui.viewmodel.jenis.DetailJenisUiState
import com.example.pam_uas_andini.ui.viewmodel.jenis.DetailJenisViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailJenisView(
    onBack: () -> Unit = { },
    onEditClick: () -> Unit = { },
    onDeleteClick: () -> Unit = { },
    modifier: Modifier = Modifier,
    viewModel: DetailJenisViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.refreshDetailJenis() // Refresh detail saat halaman ini diluncurkan
    }

    Scaffold(
        topBar = {
            CostumeTopAppBar(
                title = DestinasiDetailJenis.titleRes,
                canNavigateBack = true,
                //scrollBehavior = scrollBehavior,
                navigateUp = onBack,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onEditClick,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp),
                containerColor = colorResource(id = R.color.primary)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Kontak",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        DetailStatus(
            modifier = Modifier.padding(innerPadding),
            detailJenisUiState = viewModel.detailJenisUiState,
            onDeleteClick = {
                viewModel.deleteJenis()
                onBack()
            }
        )
    }
}

@Composable
fun DetailStatus(
    modifier: Modifier = Modifier,
    detailJenisUiState: DetailJenisUiState,
    onDeleteClick: () -> Unit
) {
    var deleteConfirmationRequired by remember { mutableStateOf(false) }

    when (detailJenisUiState) {
        is DetailJenisUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailJenisUiState.Success -> {
            val jenis = detailJenisUiState.jenis.data
            Column(modifier = modifier.padding(16.dp)) {
                ItemDetailJenis(jenis = jenis)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { deleteConfirmationRequired = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.primary)
                    )
                ) {
                    Text(text = "Delete")
                }

                if (deleteConfirmationRequired) {
                    DeleteConfirmationDialog(
                        onDeleteConfirm = {
                            deleteConfirmationRequired = false
                            onDeleteClick()
                        },
                        onDeleteCancel = { deleteConfirmationRequired = false }
                    )
                }
            }
        }
        is DetailJenisUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error loading data")
            }
        }
    }
}

@Composable
fun ItemDetailJenis(
    modifier: Modifier = Modifier,
    jenis: Jenis
) {
    Card(
        modifier = modifier.padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.carddtl)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ComponentDetailJenis(judul = "ID Jenis", isinya = jenis.id_jenis)
            ComponentDetailJenis(judul = "Nama Jenis", isinya = jenis.nama_jenis)
            ComponentDetailJenis(judul = "Deskripsi", isinya = jenis.deskripsi_jenis)
        }
    }
}

@Composable
fun ComponentDetailJenis(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
) {
    Column(modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$judul : ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = isinya,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.primary),
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text("Delete Data") },
        text = { Text("Apakah anda yakin ingin menghapus data?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes")
            }
        }
    )
}