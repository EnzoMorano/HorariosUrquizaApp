package com.example.horariosurquiza

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horariosurquiza.ui.ArrowBackVector
import com.example.horariosurquiza.ui.ArrowForwardVector
import com.example.horariosurquiza.ui.CalendarVector
import com.example.horariosurquiza.ui.HeartFilledVector
import com.example.horariosurquiza.ui.SwapVertVector
import com.example.horariosurquiza.ui.TimeVector
import com.example.horariosurquiza.ui.components.CampoSeleccionEstacion
import com.example.horariosurquiza.ui.components.SeleccionarEstacionDialog
import com.example.horariosurquiza.ui.theme.HorariosUrquizaTheme
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

// Pantalla principal contenedor que recibe el ViewModel
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(viewModel: HorariosViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                NavigationBarItem(
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = CalendarVector,
                            contentDescription = "Consulta"
                        )
                    },
                    label = { Text("Consulta") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    )
                )

                NavigationBarItem(
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = HeartFilledVector,
                            contentDescription = "Favoritos"
                        )
                    },
                    label = { Text("Favoritos") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            when (page) {
                0 -> PantallaPrincipal(viewModel = viewModel)
                1 -> PantallaFavoritos(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    viewModel: HorariosViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var origenExpanded by remember { mutableStateOf(false) }
    var destinoExpanded by remember { mutableStateOf(false) }

    // Diálogo de aviso cuando hay una nueva versión disponible para descargar
    if (viewModel.estadoActualizacion.hayActualizacion) {
        AlertDialog(
            onDismissRequest = { viewModel.descartarActualizacion() },
            title = {
                Text(
                    text = "Nueva versión disponible",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = "Hay una nueva versión (${viewModel.estadoActualizacion.versionNueva}) de la aplicación disponible. ¿Deseas descargar la actualización?",
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.estadoActualizacion.urlDescarga)).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "No se pudo abrir el enlace de descarga", Toast.LENGTH_SHORT).show()
                        }
                        viewModel.descartarActualizacion()
                    }
                ) {
                    Text(
                        text = "Actualizar",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.descartarActualizacion() }) {
                    Text("Más tarde")
                }
            }
        )
    }

    // Actualiza la hora del sistema en tiempo real siempre que se ingrese o vuelva a la pantalla de Búsqueda
    LaunchedEffect(viewModel.pantallaActual) {
        if (viewModel.pantallaActual == Pantalla.BUSQUEDA) {
            viewModel.actualizarHoraActual()
        }
    }

    BackHandler(enabled = viewModel.pantallaActual == Pantalla.RESULTADOS) {
        viewModel.volverABusqueda()
    }

    AnimatedContent(
        targetState = viewModel.pantallaActual,
        transitionSpec = {
            if (targetState == Pantalla.RESULTADOS) {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut()
                )
            } else {
                (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> width } + fadeOut()
                )
            }
        },
        label = "PantallaAnimation",
        modifier = modifier.fillMaxSize()
    ) { pantalla ->
        when (pantalla) {
            Pantalla.BUSQUEDA -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "LÍNEA URQUIZA",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    Text(
                        text = "Consulta de Horarios",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Cartel/Banner de aviso si la fecha seleccionada es un feriado
                    val feriadoSeleccionado = viewModel.obtenerFeriado(viewModel.anio, viewModel.mes, viewModel.dia)
                    if (feriadoSeleccionado != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp)
                        ) {
                            Text(
                                text = "⚠️ Feriado: ${feriadoSeleccionado.nombre}${if (feriadoSeleccionado.tipo.isNotEmpty()) " (${feriadoSeleccionado.tipo})" else ""}. Se aplican horarios de domingo.",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }
                    }

                    CampoSeleccionEstacion(
                        label = "Estación de Origen",
                        estacionSeleccionada = viewModel.estacionOrigen,
                        onAbrirDialogo = { origenExpanded = true }
                    )

                    if (origenExpanded) {
                        SeleccionarEstacionDialog(
                            titulo = "Seleccionar Origen",
                            ordenEstaciones = viewModel.ordenEstaciones,
                            estacionesRecientes = viewModel.estacionesRecientes,
                            estacionesFavoritas = viewModel.estacionesFavoritas,
                            onToggleFavorita = { viewModel.toggleFavorita(it) },
                            onDismiss = { origenExpanded = false },
                            onEstacionSeleccionada = { estacion ->
                                if (estacion == viewModel.estacionDestino) {
                                    Toast.makeText(context, "Por favor, selecciona dos estaciones distintas", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.estacionOrigen = estacion
                                    origenExpanded = false
                                    viewModel.actualizarRecientes(estacion)
                                }
                            }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { viewModel.intercambiarEstaciones() }
                        ) {
                            Icon(
                                imageVector = SwapVertVector,
                                contentDescription = "Intercambiar origen y destino",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    CampoSeleccionEstacion(
                        label = "Estación de Destino",
                        estacionSeleccionada = viewModel.estacionDestino,
                        onAbrirDialogo = { destinoExpanded = true }
                    )

                    if (destinoExpanded) {
                        SeleccionarEstacionDialog(
                            titulo = "Seleccionar Destino",
                            ordenEstaciones = viewModel.ordenEstaciones,
                            estacionesRecientes = viewModel.estacionesRecientes,
                            estacionesFavoritas = viewModel.estacionesFavoritas,
                            onToggleFavorita = { viewModel.toggleFavorita(it) },
                            onDismiss = { destinoExpanded = false },
                            onEstacionSeleccionada = { estacion ->
                                if (estacion == viewModel.estacionOrigen) {
                                    Toast.makeText(context, "Por favor, selecciona dos estaciones distintas", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.estacionDestino = estacion
                                    destinoExpanded = false
                                    viewModel.actualizarRecientes(estacion)
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                DatePickerDialog(
                                    context,
                                    { _, selectedYear, selectedMonth, selectedDay ->
                                        viewModel.anio = selectedYear
                                        viewModel.mes = selectedMonth
                                        viewModel.dia = selectedDay
                                    },
                                    viewModel.anio, viewModel.mes, viewModel.dia
                                ).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                        ) {
                            Icon(
                                imageVector = CalendarVector,
                                contentDescription = "Seleccionar Fecha",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = String.format(Locale.getDefault(), "%02d/%02d/%04d", viewModel.dia, viewModel.mes + 1, viewModel.anio),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                TimePickerDialog(
                                    context,
                                    { _, selectedHour, selectedMinute ->
                                        viewModel.hora = selectedHour
                                        viewModel.minuto = selectedMinute
                                    },
                                    viewModel.hora, viewModel.minuto, true
                                ).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                        ) {
                            Icon(
                                imageVector = TimeVector,
                                contentDescription = "Seleccionar Hora",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = String.format(Locale.getDefault(), "%02d:%02d hs", viewModel.hora, viewModel.minuto),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.ejecutarBusqueda() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            text = "Buscar horarios",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Pantalla.RESULTADOS -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Encabezado de pantalla de resultados
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewModel.volverABusqueda() }) {
                            Icon(
                                imageVector = ArrowBackVector,
                                contentDescription = "Volver al menú principal",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Text(
                            text = "Próximos Trenes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    // Cartel de aviso en la pantalla de resultados si es feriado
                    val feriadoResultado = viewModel.obtenerFeriado(viewModel.anio, viewModel.mes, viewModel.dia)
                    if (feriadoResultado != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "⚠️ Feriado: ${feriadoResultado.nombre}. Horarios de domingo.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    // Tarjeta para Origen, Intercambio y Destino
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Origen
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { origenExpanded = true },
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "Origen",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = viewModel.estacionOrigen.ifEmpty { "Seleccionar" },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }

                            // Botón Intercambio
                            IconButton(
                                onClick = { viewModel.intercambiarEstaciones() },
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(40.dp)
                            ) {
                                Icon(
                                    imageVector = SwapVertVector,
                                    contentDescription = "Intercambiar origen y destino",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            // Destino
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { destinoExpanded = true },
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "Destino",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = viewModel.estacionDestino.ifEmpty { "Seleccionar" },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    if (origenExpanded) {
                        SeleccionarEstacionDialog(
                            titulo = "Seleccionar Origen",
                            ordenEstaciones = viewModel.ordenEstaciones,
                            estacionesRecientes = viewModel.estacionesRecientes,
                            estacionesFavoritas = viewModel.estacionesFavoritas,
                            onToggleFavorita = { viewModel.toggleFavorita(it) },
                            onDismiss = { origenExpanded = false },
                            onEstacionSeleccionada = { estacion ->
                                if (estacion == viewModel.estacionDestino) {
                                    Toast.makeText(context, "Por favor, selecciona dos estaciones distintas", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.estacionOrigen = estacion
                                    origenExpanded = false
                                    viewModel.actualizarRecientes(estacion)
                                    viewModel.ejecutarBusqueda(origen = estacion)
                                }
                            }
                        )
                    }

                    if (destinoExpanded) {
                        SeleccionarEstacionDialog(
                            titulo = "Seleccionar Destino",
                            ordenEstaciones = viewModel.ordenEstaciones,
                            estacionesRecientes = viewModel.estacionesRecientes,
                            estacionesFavoritas = viewModel.estacionesFavoritas,
                            onToggleFavorita = { viewModel.toggleFavorita(it) },
                            onDismiss = { destinoExpanded = false },
                            onEstacionSeleccionada = { estacion ->
                                if (estacion == viewModel.estacionOrigen) {
                                    Toast.makeText(context, "Por favor, selecciona dos estaciones distintas", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.estacionDestino = estacion
                                    destinoExpanded = false
                                    viewModel.actualizarRecientes(estacion)
                                    viewModel.ejecutarBusqueda(destino = estacion)
                                }
                            }
                        )
                    }

                    // Botones de Fecha y Hora en Pantalla de Resultados
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                DatePickerDialog(
                                    context,
                                    { _, selectedYear, selectedMonth, selectedDay ->
                                        viewModel.anio = selectedYear
                                        viewModel.mes = selectedMonth
                                        viewModel.dia = selectedDay
                                        viewModel.ejecutarBusqueda()
                                    },
                                    viewModel.anio, viewModel.mes, viewModel.dia
                                ).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                        ) {
                            Icon(
                                imageVector = CalendarVector,
                                contentDescription = "Seleccionar Fecha",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = String.format(Locale.getDefault(), "%02d/%02d/%04d", viewModel.dia, viewModel.mes + 1, viewModel.anio),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                TimePickerDialog(
                                    context,
                                    { _, selectedHour, selectedMinute ->
                                        viewModel.hora = selectedHour
                                        viewModel.minuto = selectedMinute
                                        viewModel.ejecutarBusqueda()
                                    },
                                    viewModel.hora, viewModel.minuto, true
                                ).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                        ) {
                            Icon(
                                imageVector = TimeVector,
                                contentDescription = "Seleccionar Hora",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = String.format(Locale.getDefault(), "%02d:%02d hs", viewModel.hora, viewModel.minuto),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    if (viewModel.cargando) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Cargando próximos trenes...",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val calHoy = Calendar.getInstance()
                            val esHoy = viewModel.anio == calHoy.get(Calendar.YEAR) &&
                                    viewModel.mes == calHoy.get(Calendar.MONTH) &&
                                    viewModel.dia == calHoy.get(Calendar.DAY_OF_MONTH)

                            val minsHoraActual = calHoy.get(Calendar.HOUR_OF_DAY) * 60 + calHoy.get(Calendar.MINUTE)
                            val minsHoraSeleccionada = viewModel.hora * 60 + viewModel.minuto
                            val diffMinsSeleccionada = minsHoraSeleccionada - minsHoraActual

                            val usarFormatoRelativo = esHoy && (diffMinsSeleccionada in -10..60)

                            viewModel.listaTrenes.forEachIndexed { index, tren ->
                                val esProximo = index == 0
                                val containerColor = if (esProximo) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                }
                                val borderContainerColor = if (esProximo) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                }

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    colors = CardDefaults.cardColors(containerColor = containerColor),
                                    border = BorderStroke(2.dp, borderContainerColor)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        verticalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        if (esProximo) {
                                            Surface(
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier
                                                    .align(Alignment.CenterHorizontally)
                                                    .padding(bottom = 2.dp)
                                            ) {
                                                Text(
                                                    text = "PRÓXIMO TREN",
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 11.sp,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                                )
                                            }
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(
                                                modifier = Modifier.weight(1f),
                                                horizontalAlignment = Alignment.Start
                                            ) {
                                                Text(
                                                    text = if (usarFormatoRelativo) {
                                                        if (tren.tiempoRestanteMinutos > 0) {
                                                            "En ${tren.tiempoRestanteMinutos} min"
                                                        } else if (tren.tiempoRestanteMinutos == 0) {
                                                            "Ahora"
                                                        } else {
                                                            "${tren.horaLlegadaOrigen} hs"
                                                        }
                                                    } else {
                                                        "${tren.horaLlegadaOrigen} hs"
                                                    },
                                                    fontSize = if (esProximo) 22.sp else 19.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (esProximo) {
                                                        MaterialTheme.colorScheme.onPrimaryContainer
                                                    } else {
                                                        MaterialTheme.colorScheme.onSurfaceVariant
                                                    }
                                                )
                                                Text(
                                                    text = viewModel.estacionOrigen.ifEmpty { "Origen" },
                                                    fontSize = if (esProximo) 14.sp else 13.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = if (esProximo) {
                                                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                                    } else {
                                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
                                                    },
                                                    modifier = Modifier.padding(top = 2.dp)
                                                )
                                            }

                                            Icon(
                                                imageVector = ArrowForwardVector,
                                                contentDescription = "Hacia",
                                                tint = if (esProximo) {
                                                    MaterialTheme.colorScheme.primary
                                                } else {
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                                },
                                                modifier = Modifier
                                                    .padding(horizontal = 12.dp)
                                                    .size(24.dp)
                                            )

                                            Column(
                                                modifier = Modifier.weight(1f),
                                                horizontalAlignment = Alignment.End
                                            ) {
                                                Text(
                                                    text = "${tren.horaLlegadaDestino} hs",
                                                    fontSize = if (esProximo) 22.sp else 19.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (esProximo) {
                                                        MaterialTheme.colorScheme.onPrimaryContainer
                                                    } else {
                                                        MaterialTheme.colorScheme.onSurfaceVariant
                                                    }
                                                )
                                                Text(
                                                    text = viewModel.estacionDestino.ifEmpty { "Destino" },
                                                    fontSize = if (esProximo) 14.sp else 13.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = if (esProximo) {
                                                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                                    } else {
                                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
                                                    },
                                                    modifier = Modifier.padding(top = 2.dp)
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaPrincipalPreview() {
    HorariosUrquizaTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Text("LÍNEA URQUIZA Preview")
        }
    }
}
