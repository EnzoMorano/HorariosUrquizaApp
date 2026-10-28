package com.example.horariosurquiza

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horariosurquiza.ui.HeartBorderVector
import com.example.horariosurquiza.ui.HeartFilledVector
import com.example.horariosurquiza.ui.components.GestionarFavoritasDialog
import java.util.Calendar
import java.util.Locale

@Composable
fun PantallaFavoritos(
    viewModel: HorariosViewModel,
    modifier: Modifier = Modifier
) {
    var mostrarGestionarDialog by remember { mutableStateOf(false) }

    val calendarActual = Calendar.getInstance()
    val sufijoDia = viewModel.obtenerSufijoDia(calendarActual)
    val feriadoHoy = viewModel.obtenerFeriado(
        calendarActual.get(Calendar.YEAR),
        calendarActual.get(Calendar.MONTH),
        calendarActual.get(Calendar.DAY_OF_MONTH)
    )

    val horaActualStr = String.format(Locale.getDefault(), "%02d:%02d", calendarActual.get(Calendar.HOUR_OF_DAY), calendarActual.get(Calendar.MINUTE))

    val horariosFavoritasMap = remember(viewModel.estacionesFavoritas.toList(), viewModel.horariosCache.toMap(), viewModel.datosCargados, viewModel.feriadosCache.toMap()) {
        val map = mutableMapOf<String, Pair<String, String>>()
        if (viewModel.datosCargados) {
            val docsLacroze = viewModel.horariosCache["lacroze_$sufijoDia"] ?: emptyList()
            val docsLemos = viewModel.horariosCache["lemos_$sufijoDia"] ?: emptyList()

            viewModel.estacionesFavoritas.forEach { estacion ->
                val keyEstacion = obtenerClaveEstacion(estacion)

                val proximosLacroze = docsLacroze.mapNotNull { doc ->
                    val horaDoc = doc[keyEstacion] as? String ?: ""
                    if (horaDoc.isNotEmpty()) {
                        val diff = calcularMinutosDiferencia(horaActualStr, horaDoc)
                        Pair(horaDoc, diff)
                    } else null
                }.sortedWith(
                    compareBy<Pair<String, Int>> { it.second < 0 }
                        .thenBy { if (it.second >= 0) it.second else it.second + 1440 }
                )

                val txtLacroze = if (proximosLacroze.isNotEmpty()) {
                    val p = proximosLacroze.first()
                    formatearTiempoRestante(p.first, p.second)
                } else "Sin servicio"

                val proximosLemos = docsLemos.mapNotNull { doc ->
                    val horaDoc = doc[keyEstacion] as? String ?: ""
                    if (horaDoc.isNotEmpty()) {
                        val diff = calcularMinutosDiferencia(horaActualStr, horaDoc)
                        Pair(horaDoc, diff)
                    } else null
                }.sortedWith(
                    compareBy<Pair<String, Int>> { it.second < 0 }
                        .thenBy { if (it.second >= 0) it.second else it.second + 1440 }
                )

                val txtLemos = if (proximosLemos.isNotEmpty()) {
                    val p = proximosLemos.first()
                    formatearTiempoRestante(p.first, p.second)
                } else "Sin servicio"

                map[estacion] = Pair(txtLacroze, txtLemos)
            }
        }
        map
    }

    if (mostrarGestionarDialog) {
        GestionarFavoritasDialog(
            ordenEstaciones = viewModel.ordenEstaciones,
            estacionesFavoritas = viewModel.estacionesFavoritas,
            onToggleFavorita = { viewModel.toggleFavorita(it) },
            onDismiss = { mostrarGestionarDialog = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Button(
                onClick = { mostrarGestionarDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(
                    imageVector = HeartFilledVector,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Estaciones Favoritas (${viewModel.estacionesFavoritas.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Cartel/Banner si el día actual es feriado
        if (feriadoHoy != null) {
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = "⚠️ Hoy es Feriado (${feriadoHoy.nombre}). Se muestran horarios de domingo.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

        if (!viewModel.datosCargados && viewModel.estacionesFavoritas.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Cargando horarios...",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else if (viewModel.estacionesFavoritas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = HeartBorderVector,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No tienes estaciones favoritas seleccionadas.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { mostrarGestionarDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = "Agregar estaciones favoritas")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.estacionesFavoritas) { estacion ->
                    val horario = horariosFavoritasMap[estacion] ?: Pair("Cargando...", "Cargando...")

                    val esLacrozeTerminal = estacion.equals("Federico Lacroze", ignoreCase = true)
                    val esLemosTerminal = estacion.equals("General Lemos", ignoreCase = true)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = estacion,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Icon(
                                    imageVector = HeartFilledVector,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp))

                            if (!esLacrozeTerminal) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Sentido a Lemos:",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                    )
                                    Text(
                                        text = horario.first,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            if (!esLemosTerminal) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Sentido a Lacroze:",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                    )
                                    Text(
                                        text = horario.second,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
