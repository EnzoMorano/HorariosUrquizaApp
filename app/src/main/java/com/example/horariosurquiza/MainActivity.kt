package com.example.horariosurquiza

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.horariosurquiza.ui.theme.HorariosUrquizaTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Calendar
import java.util.Locale

// Data class para representar cada tren
data class Tren(
    val horaLlegadaOrigen: String,
    val horaLlegadaDestino: String,
    val tiempoRestanteMinutos: Int
)

// Estados de pantalla para la navegación animada
enum class Pantalla {
    BUSQUEDA,
    RESULTADOS
}

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HorariosUrquizaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaPrincipal(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Función auxiliar para mapear el nombre visible de la estación con la clave correspondiente del JSON
fun obtenerClaveEstacion(estacion: String): String {
    return when (estacion) {
        "Federico Lacroze" -> "lacroze"
        "José Artigas", "Artigas" -> "artigas"
        "Pedro N. Arata" -> "arata"
        "Doctor F. Beiro" -> "beiro"
        "El Libertador" -> "libertador"
        "Antonio Devoto" -> "devoto"
        "Coronel F. Lynch" -> "lynch"
        "Fernández Moreno" -> "moreno"
        "Lourdes" -> "lourdes"
        "Tropezón" -> "tropezon"
        "José M. Bosch" -> "bosch"
        "Martín Coronado" -> "coronado"
        "Pablo Podestá" -> "podesta"
        "Jorge Newbery" -> "newbery"
        "Rubén Dario", "Rubén Darío" -> "dario"
        "Ejército de los Andes" -> "andes"
        "Juan B. de La Salle" -> "salle"
        "Sargento Barrufaldi" -> "barrufaldi"
        "Capitán Lozano" -> "lozano"
        "Teniente Agneta" -> "agneta"
        "Campo de Mayo" -> "campo_de_mayo"
        "Sargento Cabral" -> "cabral"
        "General Lemos" -> "lemos"
        else -> estacion.lowercase()
    }
}

// Función auxiliar para calcular la diferencia en minutos entre dos horas "HH:mm"
fun calcularMinutosDiferencia(horaActualStr: String, horaTrenStr: String): Int {
    return try {
        val partesActual = horaActualStr.split(":").map { it.trim().toInt() }
        val partesTren = horaTrenStr.split(":").map { it.trim().toInt() }

        val minsActual = partesActual[0] * 60 + partesActual[1]
        val minsTren = partesTren[0] * 60 + partesTren[1]

        minsTren - minsActual
    } catch (_: Exception) {
        0
    }
}

// Componente para el campo OutlinedTextField que abre el selector
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
    onDismiss: () -> Unit,
    onEstacionSeleccionada: (String) -> Unit
) {
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
                // Cabecera con título y botón de cierre
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

                // LazyColumn con secciones Recientes y Todas las Estaciones
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    if (estacionesRecientes.isNotEmpty()) {
                        item {
                            Text(
                                text = "Recientes",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                        }
                        items(estacionesRecientes) { estacion ->
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
                        ListItem(
                            headlineContent = {
                                Text(text = estacion)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    // 1. Lista inmutable con el orden real de las estaciones de la línea Urquiza
    val ordenEstaciones = listOf(
        "Federico Lacroze", "José Artigas", "Pedro N. Arata", "Doctor F. Beiro", "El Libertador", "Antonio Devoto",
        "Coronel F. Lynch", "Fernández Moreno", "Lourdes", "Tropezón", "José M. Bosch", "Martín Coronado", "Pablo Podestá",
        "Jorge Newbery", "Rubén Dario", "Ejército de los Andes", "Juan B. de La Salle", "Sargento Barrufaldi",
        "Capitán Lozano", "Teniente Agneta", "Campo de Mayo", "Sargento Cabral", "General Lemos"
    )

    // Estados para la fecha y hora seleccionada (por defecto la fecha y hora actual al abrir la app)
    val calendarActual = remember { Calendar.getInstance() }

    var anio by remember { mutableStateOf(calendarActual.get(Calendar.YEAR)) }
    var mes by remember { mutableStateOf(calendarActual.get(Calendar.MONTH)) }
    var dia by remember { mutableStateOf(calendarActual.get(Calendar.DAY_OF_MONTH)) }

    var hora by remember { mutableStateOf(calendarActual.get(Calendar.HOUR_OF_DAY)) }
    var minuto by remember { mutableStateOf(calendarActual.get(Calendar.MINUTE)) }

    // Estados para los selectores de estación y lista de búsquedas recientes
    var estacionOrigen by remember { mutableStateOf("") }
    var estacionDestino by remember { mutableStateOf("") }

    var origenExpanded by remember { mutableStateOf(false) }
    var destinoExpanded by remember { mutableStateOf(false) }
    val estacionesRecientes = remember { mutableStateListOf<String>() }

    // Lista de resultados recuperados de Firestore
    var listaTrenes by remember { mutableStateOf<List<Tren>>(emptyList()) }

    // Estado para controlar la pantalla actual
    var pantallaActual by remember { mutableStateOf(Pantalla.BUSQUEDA) }

    fun actualizarRecientes(estacion: String, listaRecientes: MutableList<String>) {
        if (listaRecientes.contains(estacion)) {
            listaRecientes.remove(estacion) // Si ya estaba, la borramos para subirla
        }
        listaRecientes.add(0, estacion) // La insertamos en la posición 0 (arriba de todo)
        if (listaRecientes.size > 5) {
            listaRecientes.removeAt(listaRecientes.lastIndex) // Si hay 6, borramos la última
        }
    }

    // Función reutilizable para realizar la consulta a Firestore con las estaciones seleccionadas
    fun ejecutarBusqueda(origen: String = estacionOrigen, destino: String = estacionDestino) {
        val indiceOrigen = ordenEstaciones.indexOf(origen)
        val indiceDestino = ordenEstaciones.indexOf(destino)

        if (indiceOrigen == -1 || indiceDestino == -1 || indiceOrigen == indiceDestino) {
            Toast.makeText(
                context,
                "Por favor, selecciona dos estaciones distintas",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val sentidoPrefix = if (indiceOrigen < indiceDestino) "lacroze" else "lemos"

        val calSeleccionado = Calendar.getInstance().apply {
            set(anio, mes, dia)
        }
        val dayOfWeek = calSeleccionado.get(Calendar.DAY_OF_WEEK)
        val sufijoDia = when (dayOfWeek) {
            Calendar.SATURDAY -> "sabado"
            Calendar.SUNDAY -> "domingo"
            else -> "semana"
        }

        val coleccionTarget = "${sentidoPrefix}_${sufijoDia}"

        if (isPreview) {
            listaTrenes = listOf(
                Tren("08:00", "08:35", 15),
                Tren("08:15", "08:50", 30),
                Tren("08:30", "09:05", 45)
            )
            pantallaActual = Pantalla.RESULTADOS
            return
        }

        val horaActualStr = String.format(Locale.getDefault(), "%02d:%02d", hora, minuto)
        val db = Firebase.firestore

        db.collection(coleccionTarget).get()
            .addOnSuccessListener { result ->
                val keyOrigen = obtenerClaveEstacion(origen)
                val keyDestino = obtenerClaveEstacion(destino)

                val nuevosTrenes = mutableListOf<Tren>()
                for (document in result) {
                    val horaOrigenDoc = document.getString(keyOrigen) ?: ""
                    val horaDestinoDoc = document.getString(keyDestino) ?: ""

                    if (horaOrigenDoc.isNotEmpty() && horaDestinoDoc.isNotEmpty()) {
                        val diffMins = calcularMinutosDiferencia(horaActualStr, horaOrigenDoc)
                        nuevosTrenes.add(
                            Tren(
                                horaLlegadaOrigen = horaOrigenDoc,
                                horaLlegadaDestino = horaDestinoDoc,
                                tiempoRestanteMinutos = diffMins
                            )
                        )
                    }
                }

                val trenesOrdenados = nuevosTrenes.sortedWith(
                    compareBy<Tren> { it.tiempoRestanteMinutos < 0 }
                        .thenBy { if (it.tiempoRestanteMinutos >= 0) it.tiempoRestanteMinutos else it.tiempoRestanteMinutos + 1440 }
                )

                listaTrenes = trenesOrdenados.take(3)
                pantallaActual = Pantalla.RESULTADOS
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error al obtener horarios de $coleccionTarget", exception)
            }
    }

    // Transición animada entre pantallas
    AnimatedContent(
        targetState = pantallaActual,
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
                // PANTALLA 1: MENÚ PRINCIPAL DE BÚSQUEDA
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Encabezado decorativo
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

                    // Selector Estación de Origen (con diálogo a pantalla completa)
                    CampoSeleccionEstacion(
                        label = "Estación de Origen",
                        estacionSeleccionada = estacionOrigen,
                        onAbrirDialogo = { origenExpanded = true }
                    )

                    if (origenExpanded) {
                        SeleccionarEstacionDialog(
                            titulo = "Seleccionar Origen",
                            ordenEstaciones = ordenEstaciones,
                            estacionesRecientes = estacionesRecientes,
                            onDismiss = { origenExpanded = false },
                            onEstacionSeleccionada = { estacion ->
                                estacionOrigen = estacion
                                origenExpanded = false
                                actualizarRecientes(estacion, estacionesRecientes)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Selector Estación de Destino (con diálogo a pantalla completa)
                    CampoSeleccionEstacion(
                        label = "Estación de Destino",
                        estacionSeleccionada = estacionDestino,
                        onAbrirDialogo = { destinoExpanded = true }
                    )

                    if (destinoExpanded) {
                        SeleccionarEstacionDialog(
                            titulo = "Seleccionar Destino",
                            ordenEstaciones = ordenEstaciones,
                            estacionesRecientes = estacionesRecientes,
                            onDismiss = { destinoExpanded = false },
                            onEstacionSeleccionada = { estacion ->
                                estacionDestino = estacion
                                destinoExpanded = false
                                actualizarRecientes(estacion, estacionesRecientes)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Selectores de Fecha y Hora (Arriba de Buscar Horarios)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Botón de Selección de Fecha
                        OutlinedButton(
                            onClick = {
                                DatePickerDialog(
                                    context,
                                    { _, selectedYear, selectedMonth, selectedDay ->
                                        anio = selectedYear
                                        mes = selectedMonth
                                        dia = selectedDay
                                    },
                                    anio, mes, dia
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
                                text = String.format(Locale.getDefault(), "%02d/%02d/%04d", dia, mes + 1, anio),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Botón de Selección de Hora
                        OutlinedButton(
                            onClick = {
                                TimePickerDialog(
                                    context,
                                    { _, selectedHour, selectedMinute ->
                                        hora = selectedHour
                                        minuto = selectedMinute
                                    },
                                    hora, minuto, true
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
                                text = String.format(Locale.getDefault(), "%02d:%02d hs", hora, minuto),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón "Buscar horarios" con lógica dinámica según el sentido y el día
                    Button(
                        onClick = {
                            ejecutarBusqueda()
                        },
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
                // PANTALLA 2: SEGUNDA PANTALLA DE RESULTADOS
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Flecha arriba para volver al menú principal
                    TopAppBar(
                        title = {
                            Text(
                                text = "Próximos Trenes",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { pantallaActual = Pantalla.BUSQUEDA }) {
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

                    // Sección superior: Selectores arriba
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        CampoSeleccionEstacion(
                            label = "Estación de Origen",
                            estacionSeleccionada = estacionOrigen,
                            onAbrirDialogo = { origenExpanded = true }
                        )

                        if (origenExpanded) {
                            SeleccionarEstacionDialog(
                                titulo = "Seleccionar Origen",
                                ordenEstaciones = ordenEstaciones,
                                estacionesRecientes = estacionesRecientes,
                                onDismiss = { origenExpanded = false },
                                onEstacionSeleccionada = { estacion ->
                                    estacionOrigen = estacion
                                    origenExpanded = false
                                    actualizarRecientes(estacion, estacionesRecientes)
                                    ejecutarBusqueda(origen = estacion)
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        CampoSeleccionEstacion(
                            label = "Estación de Destino",
                            estacionSeleccionada = estacionDestino,
                            onAbrirDialogo = { destinoExpanded = true }
                        )

                        if (destinoExpanded) {
                            SeleccionarEstacionDialog(
                                titulo = "Seleccionar Destino",
                                ordenEstaciones = ordenEstaciones,
                                estacionesRecientes = estacionesRecientes,
                                onDismiss = { destinoExpanded = false },
                                onEstacionSeleccionada = { estacion ->
                                    estacionDestino = estacion
                                    destinoExpanded = false
                                    actualizarRecientes(estacion, estacionesRecientes)
                                    ejecutarBusqueda(destino = estacion)
                                }
                            )
                        }
                    }

                    // Sección inferior: Lista de próximos trenes ocupando proporcionalmente el espacio vertical restante
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        listaTrenes.forEachIndexed { index, tren ->
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
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        ) {
                                            Text(
                                                text = "PRÓXIMO TREN",
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Lado Izquierdo: En cuánto llega + Estación Origen
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Text(
                                                text = if (tren.tiempoRestanteMinutos > 0) {
                                                    "En ${tren.tiempoRestanteMinutos} min"
                                                } else if (tren.tiempoRestanteMinutos == 0) {
                                                    "Sale ahora"
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
                                                text = estacionOrigen.ifEmpty { "Origen" },
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

                                        // En el Medio: Flecha hacia la derecha con tinte de alto contraste en modo claro/oscuro
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

                                        // Lado Derecho: Hora a la que llega al destino HH:MM + Estación Destino
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
                                                text = estacionDestino.ifEmpty { "Destino" },
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

@Preview(showBackground = true)
@Composable
fun PantallaPrincipalPreview() {
    HorariosUrquizaTheme {
        PantallaPrincipal()
    }
}
