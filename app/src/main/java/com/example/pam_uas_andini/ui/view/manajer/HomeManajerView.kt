package com.example.pam_uas_andini.ui.view.manajer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pam_uas_andini.R
import com.example.pam_uas_andini.customwidget.CostumeTopAppBar
import com.example.pam_uas_andini.model.Manajer
import com.example.pam_uas_andini.model.Pemilik
import com.example.pam_uas_andini.navigation.DestinasiHomeManajer
import com.example.pam_uas_andini.navigation.DestinasiHomePemilik
import com.example.pam_uas_andini.ui.viewmodel.PenyediaViewModel
import com.example.pam_uas_andini.ui.viewmodel.manajer.HomeManajerUiState
import com.example.pam_uas_andini.ui.viewmodel.manajer.HomeManajerViewModel
import com.example.pam_uas_andini.ui.viewmodel.pemilik.HomePemilikUiState
import com.example.pam_uas_andini.ui.viewmodel.pemilik.HomePemilikViewModel
@Composable
fun HomeStatus(
    homeManajerUiState: HomeManajerUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Manajer) -> Unit = {},
    onDetailClick: (String) -> Unit
) {
    when(homeManajerUiState) {
        is HomeManajerUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeManajerUiState.Success ->
            if (homeManajerUiState.manajer.isEmpty()) {
                return Box (modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data manajer")
                }
            } else {
                MnjLayout(
                    manajer = homeManajerUiState.manajer, modifier = modifier.fillMaxWidth(),
                    onDetailClick = {
                        onDetailClick(it.id_manajer)
                    },
                    onDeleteClick = {
                        onDeleteClick(it)
                    }
                )
            }
        is HomeManajerUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column (
        modifier =  modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun MnjLayout(
    manajer: List<Manajer>,
    modifier: Modifier = Modifier,
    onDetailClick: (Manajer) -> Unit,
    onDeleteClick: (Manajer) -> Unit = {}
) {
    LazyColumn (
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(manajer) { manajer ->
            MnjCard(
                manajer = manajer,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(manajer) },
                onDeleteClick = {
                    onDeleteClick(manajer)
                }
            )
        }
    }
}

@Composable
fun MnjCard(
    manajer: Manajer,
    modifier: Modifier = Modifier,
    onDeleteClick: (Manajer) -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column (
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = manajer.nama_manajer,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { onDeleteClick(manajer) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )
                }
            }

            Column {
                Text(
                    text = manajer.id_manajer,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}