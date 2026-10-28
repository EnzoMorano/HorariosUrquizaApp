package com.example.horariosurquiza.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.horariosurquiza.ui.HeartBorderVector
import com.example.horariosurquiza.ui.HeartFilledVector

// Componente para el campo OutlinedTextField
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoSeleccionEstacion(
    label: String,
    estacionSeleccionada: String,
    onAbrirDialogo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) {
                onAbrirDialogo()
            }
        }
    }

    OutlinedTextField(
        value = estacionSeleccionada,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
        },
        interactionSource = interactionSource,
        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = modifier.fillMaxWidth()
    )
}

// Diálogo a pantalla completa estilo Uber para la selección de estación
@Composable
fun SeleccionarEstacionDialog(
    titulo: String,
    ordenEstaciones: List<String>,
    estacionesRecientes: List<String>,
    estacionesFavoritas: List<String>,
    onToggleFavorita: (String) -> Unit,
    onDismiss: () -> Unit,
    onEstacionSeleccionada: (String) -> Unit
) {
    val recientesSinFavoritas = remember(estacionesRecientes.toList(), estacionesFavoritas.toList()) {
        estacionesRecientes.filter { !estacionesFavoritas.contains(it) }.take(3)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = titulo,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Cerrar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    if (estacionesFavoritas.isNotEmpty()) {
                        item {
                            Text(
                                text = "Estaciones Favoritas",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                        }
                        items(estacionesFavoritas) { estacion ->
                            ListItem(
                                headlineContent = {
                                    Text(text = estacion, fontWeight = FontWeight.SemiBold)
                                },
                                leadingContent = {
                                    Icon(
                                        imageVector = HeartFilledVector,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                modifier = Modifier.clickable {
                                    onEstacionSeleccionada(estacion)
                                }
                            )
                        }
                        item {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }

                    if (recientesSinFavoritas.isNotEmpty()) {
                        item {
                            Text(
                                text = "Estaciones Recientes",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                        }
                        items(recientesSinFavoritas) { estacion ->
                            ListItem(
                                headlineContent = {
                                    Text(text = estacion, fontWeight = FontWeight.Medium)
                                },
                                modifier = Modifier.clickable {
                                    onEstacionSeleccionada(estacion)
                                }
                            )
                        }
                        item {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }

                    item {
                        Text(
                            text = "Todas las estaciones",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }

                    items(ordenEstaciones) { estacion ->
                        val esFavorita = estacionesFavoritas.contains(estacion)
                        ListItem(
                            headlineContent = {
                                Text(text = estacion)
                            },
                            trailingContent = {
                                IconButton(onClick = { onToggleFavorita(estacion) }) {
                                    Icon(
                                        imageVector = if (esFavorita) HeartFilledVector else HeartBorderVector,
                                        contentDescription = null,
                                        tint = if (esFavorita) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            modifier = Modifier.clickable {
                                onEstacionSeleccionada(estacion)
                            }
                        )
                    }
                }
            }
        }
    }
}

// Diálogo para gestionar la selección de Estaciones Favoritas
@Composable
fun GestionarFavoritasDialog(
    ordenEstaciones: List<String>,
    estacionesFavoritas: List<String>,
    onToggleFavorita: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val estacionesOrdenadasDialog = remember(estacionesFavoritas.toList(), ordenEstaciones) {
        ordenEstaciones.filter { estacionesFavoritas.contains(it) } + ordenEstaciones.filter { !estacionesFavoritas.contains(it) }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Seleccionar Favoritas",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Listo",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(estacionesOrdenadasDialog) { estacion ->
                        val esFavorita = estacionesFavoritas.contains(estacion)
                        ListItem(
                            headlineContent = {
                                Text(text = estacion, fontWeight = if (esFavorita) FontWeight.Bold else FontWeight.Normal)
                            },
                            trailingContent = {
                                IconButton(onClick = { onToggleFavorita(estacion) }) {
                                    Icon(
                                        imageVector = if (esFavorita) HeartFilledVector else HeartBorderVector,
                                        contentDescription = "Favorita",
                                        tint = if (esFavorita) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            modifier = Modifier.clickable { onToggleFavorita(estacion) }
                        )
                    }
                }
            }
        }
    }
}
