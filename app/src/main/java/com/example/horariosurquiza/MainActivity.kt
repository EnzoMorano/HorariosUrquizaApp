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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
                fill = SolidColor(Color.Unspecified),
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
            path(fill = SolidColor(Color.Unspecified)) {
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
            path(fill = SolidColor(Color.Unspecified)) {
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

    // Estados para los selectores de estación
    var estacionOrigen by remember { mutableStateOf("") }
    var estacionDestino by remember { mutableStateOf("") }

    var origenExpanded by remember { mutableStateOf(false) }
    var destinoExpanded by remember { mutableStateOf(false) }

    // Lista de resultados recuperados de Firestore
    var listaTrenes by remember { mutableStateOf<List<Tren>>(emptyList()) }

    // Estado para controlar la pantalla actual
    var pantallaActual by remember { mutableStateOf(Pantalla.BUSQUEDA) }

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

                    // Selector Estación de Origen
                    ExposedDropdownMenuBox(
                        expanded = origenExpanded,
                        onExpandedChange = { origenExpanded = !origenExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = estacionOrigen,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Estación de Origen") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = origenExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                            ),
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = origenExpanded,
                            onDismissRequest = { origenExpanded = false }
                        ) {
                            ordenEstaciones.forEach { estacion ->
                                DropdownMenuItem(
                                    text = { Text(estacion) },
                                    onClick = {
                                        estacionOrigen = estacion
                                        origenExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Selector Estación de Destino
                    ExposedDropdownMenuBox(
                        expanded = destinoExpanded,
                        onExpandedChange = { destinoExpanded = !destinoExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = estacionDestino,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Estación de Destino") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = destinoExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                            ),
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = destinoExpanded,
                            onDismissRequest = { destinoExpanded = false }
                        ) {
                            ordenEstaciones.forEach { estacion ->
                                DropdownMenuItem(
                                    text = { Text(estacion) },
                                    onClick = {
                                        estacionDestino = estacion
                                        destinoExpanded = false
                                    }
                                )
                            }
                        }
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
                            // 1. Obtención de índices
                            val indiceOrigen = ordenEstaciones.indexOf(estacionOrigen)
                            val indiceDestino = ordenEstaciones.indexOf(estacionDestino)

                            // 2. Validación de estaciones
                            if (indiceOrigen == -1 || indiceDestino == -1 || indiceOrigen == indiceDestino) {
                                Toast.makeText(
                                    context,
                                    "Por favor, selecciona dos estaciones distintas",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            // 3. Determinación del prefijo según el sentido del viaje
                            val sentidoPrefix = if (indiceOrigen < indiceDestino) "lacroze" else "lemos"

                            // 4. Determinación del sufijo según el día de la semana
                            val calSeleccionado = Calendar.getInstance().apply {
                                set(anio, mes, dia)
                            }
                            val dayOfWeek = calSeleccionado.get(Calendar.DAY_OF_WEEK)
                            val sufijoDia = when (dayOfWeek) {
                                Calendar.SATURDAY -> "sabado"
                                Calendar.SUNDAY -> "domingo"
                                else -> "semana"
                            }

                            // Nombre de la colección dinámica (ej. lacroze_semana, lemos_sabado, etc.)
                            val coleccionTarget = "${sentidoPrefix}_${sufijoDia}"

                            if (isPreview) {
                                // Para vista previa en Android Studio
                                listaTrenes = listOf(
                                    Tren("08:00", "08:35", 15),
                                    Tren("08:15", "08:50", 30)
                                )
                                pantallaActual = Pantalla.RESULTADOS
                                return@Button
                            }

                            // 5. Consulta dinámica a Firestore
                            val horaActualStr = String.format(Locale.getDefault(), "%02d:%02d", hora, minuto)
                            val db = Firebase.firestore

                            db.collection(coleccionTarget).get()
                                .addOnSuccessListener { result ->
                                    val keyOrigen = obtenerClaveEstacion(estacionOrigen)
                                    val keyDestino = obtenerClaveEstacion(estacionDestino)

                                    val nuevosTrenes = mutableListOf<Tren>()
                                    for (document in result) {
                                        val horaOrigen = document.getString(keyOrigen) ?: ""
                                        val horaDestino = document.getString(keyDestino) ?: ""

                                        if (horaOrigen.isNotEmpty() && horaDestino.isNotEmpty()) {
                                            val diffMins = calcularMinutosDiferencia(horaActualStr, horaOrigen)
                                            nuevosTrenes.add(
                                                Tren(
                                                    horaLlegadaOrigen = horaOrigen,
                                                    horaLlegadaDestino = horaDestino,
                                                    tiempoRestanteMinutos = diffMins
                                                )
                                            )
                                        }
                                    }

                                    // Ordenamos para que los trenes futuros (tiempoRestanteMinutos >= 0) aparezcan primero
                                    val trenesOrdenados = nuevosTrenes.sortedWith(
                                        compareBy<Tren> { it.tiempoRestanteMinutos < 0 }
                                            .thenBy { if (it.tiempoRestanteMinutos >= 0) it.tiempoRestanteMinutos else it.tiempoRestanteMinutos + 1440 }
                                    )

                                    listaTrenes = trenesOrdenados
                                    pantallaActual = Pantalla.RESULTADOS
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("Firestore", "Error al obtener horarios de $coleccionTarget", exception)
                                }
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
                        ExposedDropdownMenuBox(
                            expanded = origenExpanded,
                            onExpandedChange = { origenExpanded = !origenExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = estacionOrigen,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Estación de Origen") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = origenExpanded) },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                ),
                                modifier = Modifier
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = origenExpanded,
                                onDismissRequest = { origenExpanded = false }
                            ) {
                                ordenEstaciones.forEach { estacion ->
                                    DropdownMenuItem(
                                        text = { Text(estacion) },
                                        onClick = {
                                            estacionOrigen = estacion
                                            origenExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        ExposedDropdownMenuBox(
                            expanded = destinoExpanded,
                            onExpandedChange = { destinoExpanded = !destinoExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = estacionDestino,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Estación de Destino") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = destinoExpanded) },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                ),
                                modifier = Modifier
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = destinoExpanded,
                                onDismissRequest = { destinoExpanded = false }
                            ) {
                                ordenEstaciones.forEach { estacion ->
                                    DropdownMenuItem(
                                        text = { Text(estacion) },
                                        onClick = {
                                            estacionDestino = estacion
                                            destinoExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Sección inferior: Lista de próximos trenes desde Firestore
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        itemsIndexed(listaTrenes) { index, tren ->
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
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = containerColor),
                                border = BorderStroke(2.dp, borderContainerColor)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    if (esProximo) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.padding(bottom = 8.dp)
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
                                        Column {
                                            Text(
                                                text = if (tren.tiempoRestanteMinutos > 0) {
                                                    "En ${tren.tiempoRestanteMinutos} min"
                                                } else if (tren.tiempoRestanteMinutos == 0) {
                                                    "Sale ahora"
                                                } else {
                                                    "Horario programado"
                                                },
                                                fontSize = if (esProximo) 22.sp else 18.sp,
                                                fontWeight = if (esProximo) FontWeight.Bold else FontWeight.SemiBold,
                                                color = if (esProximo) {
                                                    MaterialTheme.colorScheme.onPrimaryContainer
                                                } else {
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                                }
                                            )
                                            Text(
                                                text = "Origen: ${tren.horaLlegadaOrigen} hs  →  Destino: ${tren.horaLlegadaDestino} hs",
                                                fontSize = 14.sp,
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
