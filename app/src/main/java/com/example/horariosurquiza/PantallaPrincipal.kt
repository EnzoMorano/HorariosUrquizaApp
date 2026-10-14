package com.example.horariosurquiza

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.horariosurquiza.ui.theme.HorariosUrquizaTheme
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

// Icono vectorial de flecha hacia atrás (ArrowBack)
private var cachedArrowBack: ImageVector? = null
val ArrowBackVector: ImageVector
    get() {
        if (cachedArrowBack != null) return cachedArrowBack!!
        cachedArrowBack = ImageVector.Builder(
            name = "ArrowBack",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20.0f, 11.0f)
                horizontalLineTo(7.83f)
                lineTo(13.42f, 5.41f)
                lineTo(12.0f, 4.0f)
                lineTo(4.0f, 12.0f)
                lineTo(12.0f, 20.0f)
                lineTo(13.41f, 18.59f)
                lineTo(7.83f, 13.0f)
                horizontalLineTo(20.0f)
                verticalLineTo(11.0f)
                close()
            }
        }.build()
        return cachedArrowBack!!
    }

// Icono vectorial de flecha hacia la derecha (ArrowForward)
private var cachedArrowForward: ImageVector? = null
val ArrowForwardVector: ImageVector
    get() {
        if (cachedArrowForward != null) return cachedArrowForward!!
        cachedArrowForward = ImageVector.Builder(
            name = "ArrowForward",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12.0f, 4.0f)
                lineTo(10.59f, 5.41f)
                lineTo(16.17f, 11.0f)
                horizontalLineTo(4.0f)
                verticalLineTo(13.0f)
                horizontalLineTo(16.17f)
                lineTo(10.59f, 18.59f)
                lineTo(12.0f, 20.0f)
                lineTo(20.0f, 12.0f)
                lineTo(12.0f, 4.0f)
                close()
            }
        }.build()
        return cachedArrowForward!!
    }

// Icono vectorial de calendario
private var cachedCalendarIcon: ImageVector? = null
val CalendarVector: ImageVector
    get() {
        if (cachedCalendarIcon != null) return cachedCalendarIcon!!
        cachedCalendarIcon = ImageVector.Builder(
            name = "CalendarIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(19.0f, 4.0f)
                horizontalLineTo(18.0f)
                verticalLineTo(2.0f)
                horizontalLineTo(16.0f)
                verticalLineTo(4.0f)
                horizontalLineTo(8.0f)
                verticalLineTo(2.0f)
                horizontalLineTo(6.0f)
                verticalLineTo(4.0f)
                horizontalLineTo(5.0f)
                lineTo(3.0f, 6.0f)
                lineTo(3.0f, 20.0f)
                lineTo(5.0f, 22.0f)
                horizontalLineTo(19.0f)
                lineTo(21.0f, 20.0f)
                verticalLineTo(6.0f)
                close()
                moveTo(19.0f, 20.0f)
                horizontalLineTo(5.0f)
                verticalLineTo(10.0f)
                horizontalLineTo(19.0f)
                verticalLineTo(20.0f)
                close()
            }
        }.build()
        return cachedCalendarIcon!!
    }

// Icono vectorial de reloj
private var cachedTimeIcon: ImageVector? = null
val TimeVector: ImageVector
    get() {
        if (cachedTimeIcon != null) return cachedTimeIcon!!
        cachedTimeIcon = ImageVector.Builder(
            name = "TimeIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(11.99f, 2.0f)
                lineTo(2.0f, 12.0f)
                lineTo(11.99f, 22.0f)
                lineTo(22.0f, 12.0f)
                close()
                moveTo(12.0f, 20.0f)
                lineTo(4.0f, 12.0f)
                lineTo(12.0f, 4.0f)
                lineTo(20.0f, 12.0f)
                close()
                moveTo(12.5f, 7.0f)
                horizontalLineTo(11.0f)
                verticalLineTo(13.0f)
                lineTo(16.25f, 16.15f)
                lineTo(17.0f, 14.92f)
                lineTo(12.5f, 12.25f)
                verticalLineTo(7.0f)
                close()
            }
        }.build()
        return cachedTimeIcon!!
    }

// Icono de corazón lleno con curva Bézier suave (Favorito activo)
private var cachedHeartFilled: ImageVector? = null
val HeartFilledVector: ImageVector
    get() {
        if (cachedHeartFilled != null) return cachedHeartFilled!!
        cachedHeartFilled = ImageVector.Builder(
            name = "HeartFilled",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12.0f, 21.35f)
                lineTo(10.55f, 20.03f)
                curveTo(5.4f, 15.36f, 2.0f, 12.28f, 2.0f, 8.5f)
                curveTo(2.0f, 5.42f, 4.42f, 3.0f, 7.5f, 3.0f)
                curveTo(9.24f, 3.0f, 10.91f, 3.81f, 12.0f, 5.09f)
                curveTo(13.09f, 3.81f, 14.76f, 3.0f, 16.5f, 3.0f)
                curveTo(19.58f, 3.0f, 22.0f, 5.42f, 22.0f, 8.5f)
                curveTo(22.0f, 12.28f, 18.6f, 15.36f, 13.45f, 20.04f)
                lineTo(12.0f, 21.35f)
                close()
            }
        }.build()
        return cachedHeartFilled!!
    }

// Icono de corazón borde estilizado (Favorito inactivo)
private var cachedHeartBorder: ImageVector? = null
val HeartBorderVector: ImageVector
    get() {
        if (cachedHeartBorder != null) return cachedHeartBorder!!
        cachedHeartBorder = ImageVector.Builder(
            name = "HeartBorder",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(16.5f, 3.0f)
                curveTo(14.76f, 3.0f, 13.09f, 3.81f, 12.0f, 5.09f)
                curveTo(10.91f, 3.81f, 9.24f, 3.0f, 7.5f, 3.0f)
                curveTo(4.42f, 3.0f, 2.0f, 5.42f, 2.0f, 8.5f)
                curveTo(2.0f, 12.28f, 5.4f, 15.36f, 10.55f, 20.03f)
                lineTo(12.0f, 21.35f)
                lineTo(13.45f, 20.03f)
                curveTo(18.6f, 15.36f, 22.0f, 12.28f, 22.0f, 8.5f)
                curveTo(22.0f, 5.42f, 19.58f, 3.0f, 16.5f, 3.0f)
                close()
                moveTo(12.1f, 18.55f)
                lineTo(12.0f, 18.65f)
                lineTo(11.9f, 18.55f)
                curveTo(7.14f, 14.24f, 4.0f, 11.39f, 4.0f, 8.5f)
                curveTo(4.0f, 6.5f, 5.5f, 5.0f, 7.5f, 5.0f)
                curveTo(9.04f, 5.0f, 10.54f, 5.99f, 11.07f, 7.36f)
                horizontalLineTo(12.93f)
                curveTo(13.46f, 5.99f, 14.96f, 5.0f, 16.5f, 5.0f)
                curveTo(18.5f, 5.0f, 20.0f, 6.5f, 20.0f, 8.5f)
                curveTo(20.0f, 11.39f, 16.86f, 14.24f, 12.1f, 18.55f)
                close()
            }
        }.build()
        return cachedHeartBorder!!
    }

// Icono vectorial para intercambiar estación origen y destino (SwapVert)
private var cachedSwapVert: ImageVector? = null
val SwapVertVector: ImageVector
    get() {
        if (cachedSwapVert != null) return cachedSwapVert!!
        cachedSwapVert = ImageVector.Builder(
            name = "SwapVert",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(16.01f, 11.0f)
                horizontalLineTo(18.0f)
                lineTo(15.0f, 6.0f)
                lineTo(12.0f, 11.0f)
                horizontalLineTo(14.01f)
                verticalLineTo(18.0f)
                horizontalLineTo(16.01f)
                verticalLineTo(11.0f)
                close()
                moveTo(9.0f, 13.0f)
                horizontalLineTo(7.0f)
                verticalLineTo(6.0f)
                horizontalLineTo(5.0f)
                verticalLineTo(13.0f)
                horizontalLineTo(3.0f)
                lineTo(6.0f, 18.0f)
                lineTo(9.0f, 13.0f)
                close()
            }
        }.build()
        return cachedSwapVert!!
    }

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
                        modifier = Modifier.padding(bottom = 28.dp)
                    )

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
                    TopAppBar(
                        title = {
                            Text(
                                text = "Próximos Trenes",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { viewModel.volverABusqueda() }) {
                                Icon(
                                    imageVector = ArrowBackVector,
                                    contentDescription = "Volver al menú principal",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
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
                                        viewModel.ejecutarBusqueda(origen = estacion)
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
                                    modifier = Modifier.size(24.dp)
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
                                        viewModel.ejecutarBusqueda(destino = estacion)
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

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
                                            viewModel.ejecutarBusqueda()
                                        },
                                        viewModel.anio, viewModel.mes, viewModel.dia
                                    ).show()
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
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
                                    .height(48.dp)
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
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                                            .padding(horizontal = 20.dp, vertical = 18.dp),
                                        verticalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        if (esProximo) {
                                            Surface(
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier
                                                    .align(Alignment.CenterHorizontally)
                                                    .padding(bottom = 4.dp)
                                            ) {
                                                Text(
                                                    text = "PRÓXIMO TREN",
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 12.sp,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
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
                                                    fontSize = if (esProximo) 24.sp else 20.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (esProximo) {
                                                        MaterialTheme.colorScheme.onPrimaryContainer
                                                    } else {
                                                        MaterialTheme.colorScheme.onSurfaceVariant
                                                    }
                                                )
                                                Text(
                                                    text = viewModel.estacionOrigen.ifEmpty { "Origen" },
                                                    fontSize = if (esProximo) 15.sp else 14.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = if (esProximo) {
                                                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                                    } else {
                                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
                                                    },
                                                    modifier = Modifier.padding(top = 4.dp)
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
                                                    .padding(horizontal = 16.dp)
                                                    .size(28.dp)
                                            )

                                            Column(
                                                modifier = Modifier.weight(1f),
                                                horizontalAlignment = Alignment.End
                                            ) {
                                                Text(
                                                    text = "${tren.horaLlegadaDestino} hs",
                                                    fontSize = if (esProximo) 24.sp else 20.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (esProximo) {
                                                        MaterialTheme.colorScheme.onPrimaryContainer
                                                    } else {
                                                        MaterialTheme.colorScheme.onSurfaceVariant
                                                    }
                                                )
                                                Text(
                                                    text = viewModel.estacionDestino.ifEmpty { "Destino" },
                                                    fontSize = if (esProximo) 15.sp else 14.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = if (esProximo) {
                                                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                                    } else {
                                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
                                                    },
                                                    modifier = Modifier.padding(top = 4.dp)
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

// Pantalla de Estaciones Favoritas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFavoritos(
    viewModel: HorariosViewModel,
    modifier: Modifier = Modifier
) {
    var mostrarGestionarDialog by remember { mutableStateOf(false) }

    val calendarActual = Calendar.getInstance()
    val dayOfWeek = calendarActual.get(Calendar.DAY_OF_WEEK)
    val sufijoDia = when (dayOfWeek) {
        Calendar.SATURDAY -> "sabado"
        Calendar.SUNDAY -> "domingo"
        else -> "semana"
    }

    val horaActualStr = String.format(Locale.getDefault(), "%02d:%02d", calendarActual.get(Calendar.HOUR_OF_DAY), calendarActual.get(Calendar.MINUTE))

    val horariosFavoritasMap = remember(viewModel.estacionesFavoritas.toList(), viewModel.horariosCache.toMap(), viewModel.datosCargados) {
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
                                        text = "Sentido Lacroze:",
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
                                        text = "Sentido Lemos:",
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

@Preview(showBackground = true)
@Composable
fun PantallaPrincipalPreview() {
    HorariosUrquizaTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Text("LÍNEA URQUIZA Preview")
        }
    }
}
