package com.example.horariosurquiza

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class HorariosViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("horarios_urquiza_prefs", Context.MODE_PRIVATE)

    // Lista inmutable con el orden real de las estaciones de la línea Urquiza
    val ordenEstaciones = listOf(
        "Federico Lacroze", "José Artigas", "Pedro N. Arata", "Doctor F. Beiro", "El Libertador", "Antonio Devoto",
        "Coronel F. Lynch", "Fernández Moreno", "Lourdes", "Tropezón", "José M. Bosch", "Martín Coronado", "Pablo Podestá",
        "Jorge Newbery", "Rubén Dario", "Ejército de los Andes", "Juan B. de La Salle", "Sargento Barrufaldi",
        "Capitán Lozano", "Teniente Agneta", "Campo de Mayo", "Sargento Cabral", "General Lemos"
    )

    // PASO 1: Variables de memoria para la caché local de colecciones y bandera de estado de carga
    val horariosCache = mutableStateMapOf<String, List<Map<String, Any>>>()
    var datosCargados by mutableStateOf(false)
        private set

    // Estados de navegación y selecciones de usuario
    var pantallaActual by mutableStateOf(Pantalla.BUSQUEDA)
    var cargando by mutableStateOf(false)

    var estacionOrigen by mutableStateOf("")
    var estacionDestino by mutableStateOf("")

    // Estados de Fecha y Hora (por defecto inicializados al momento actual del sistema)
    private val calendarActual = Calendar.getInstance()
    var anio by mutableStateOf(calendarActual.get(Calendar.YEAR))
    var mes by mutableStateOf(calendarActual.get(Calendar.MONTH))
    var dia by mutableStateOf(calendarActual.get(Calendar.DAY_OF_MONTH))

    var hora by mutableStateOf(calendarActual.get(Calendar.HOUR_OF_DAY))
    var minuto by mutableStateOf(calendarActual.get(Calendar.MINUTE))

    // Listas persistentes de Favoritas y Recientes
    val estacionesFavoritas = mutableStateListOf<String>()
    val estacionesRecientes = mutableStateListOf<String>()

    // Lista de resultados recuperados
    var listaTrenes by mutableStateOf<List<Tren>>(emptyList())
        private set

    init {
        cargarListasPersistidas()
        precargarCacheFirestore()
    }

    // Actualiza la fecha y hora con el momento exacto actual del sistema
    fun actualizarHoraActual() {
        val cal = Calendar.getInstance()
        anio = cal.get(Calendar.YEAR)
        mes = cal.get(Calendar.MONTH)
        dia = cal.get(Calendar.DAY_OF_MONTH)
        hora = cal.get(Calendar.HOUR_OF_DAY)
        minuto = cal.get(Calendar.MINUTE)
    }

    private fun cargarListasPersistidas() {
        val savedFavs = prefs.getStringSet("KEY_FAVORITAS", emptySet()) ?: emptySet()
        estacionesFavoritas.clear()
        estacionesFavoritas.addAll(savedFavs)

        val savedRecientsStr = prefs.getString("KEY_RECIENTES", "") ?: ""
        estacionesRecientes.clear()
        if (savedRecientsStr.isNotEmpty()) {
            estacionesRecientes.addAll(savedRecientsStr.split(";"))
        }
    }

    private fun guardarFavoritas() {
        prefs.edit().putStringSet("KEY_FAVORITAS", estacionesFavoritas.toSet()).apply()
    }

    private fun guardarRecientes() {
        prefs.edit().putString("KEY_RECIENTES", estacionesRecientes.joinToString(";")).apply()
    }

    // PASO 2: Descarga asíncrona al inicio que itera sobre las 6 colecciones usando viewModelScope
    private fun precargarCacheFirestore() {
        viewModelScope.launch {
            val db = Firebase.firestore
            val colecciones = listOf(
                "lemos_semana", "lacroze_semana",
                "lemos_sabado", "lacroze_sabado",
                "lemos_domingo", "lacroze_domingo"
            )

            var coleccionesDescargadas = 0
            colecciones.forEach { coleccion ->
                db.collection(coleccion).get()
                    .addOnSuccessListener { result ->
                        val docsData = result.documents.mapNotNull { it.data }
                        horariosCache[coleccion] = docsData
                        coleccionesDescargadas++

                        if (coleccionesDescargadas == colecciones.size) {
                            datosCargados = true
                            if (cargando) {
                                ejecutarBusqueda()
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FirestoreCache", "Error al precargar colección $coleccion", exception)
                    }
            }
        }
    }

    // Alternar estado de favorita de una estación
    fun toggleFavorita(estacion: String) {
        if (estacionesFavoritas.contains(estacion)) {
            estacionesFavoritas.remove(estacion)
        } else {
            estacionesFavoritas.add(estacion)
            estacionesRecientes.remove(estacion)
        }
        guardarFavoritas()
        guardarRecientes()
    }

    // Actualizar historial de estaciones recientes
    fun actualizarRecientes(estacion: String) {
        if (estacionesFavoritas.contains(estacion)) return
        if (estacionesRecientes.contains(estacion)) {
            estacionesRecientes.remove(estacion)
        }
        estacionesRecientes.add(0, estacion)
        if (estacionesRecientes.size > 3) {
            estacionesRecientes.removeAt(estacionesRecientes.lastIndex)
        }
        guardarRecientes()
    }

    // Intercambiar Estación Origen y Destino
    fun intercambiarEstaciones() {
        if (estacionOrigen.isNotEmpty() || estacionDestino.isNotEmpty()) {
            val temp = estacionOrigen
            estacionOrigen = estacionDestino
            estacionDestino = temp

            if (pantallaActual == Pantalla.RESULTADOS) {
                ejecutarBusqueda()
            }
        }
    }

    // PASO 3: Búsqueda offline que consulta directamente la memoria (horariosCache) sin llamadas a Firestore
    fun ejecutarBusqueda(origen: String = estacionOrigen, destino: String = estacionDestino) {
        val indiceOrigen = ordenEstaciones.indexOf(origen)
        val indiceDestino = ordenEstaciones.indexOf(destino)

        if (indiceOrigen == -1 || indiceDestino == -1 || indiceOrigen == indiceDestino) {
            Toast.makeText(
                getApplication(),
                "Por favor, selecciona dos estaciones distintas",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        pantallaActual = Pantalla.RESULTADOS

        if (!datosCargados) {
            cargando = true
            return
        }

        cargando = false
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

        val docsData = horariosCache[coleccionTarget] ?: emptyList()
        val keyOrigen = obtenerClaveEstacion(origen)
        val keyDestino = obtenerClaveEstacion(destino)
        val horaActualStr = String.format(Locale.getDefault(), "%02d:%02d", hora, minuto)

        val nuevosTrenes = mutableListOf<Tren>()
        for (doc in docsData) {
            val horaOrigenDoc = doc[keyOrigen] as? String ?: ""
            val horaDestinoDoc = doc[keyDestino] as? String ?: ""

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
    }

    fun volverABusqueda() {
        actualizarHoraActual()
        pantallaActual = Pantalla.BUSQUEDA
    }
}
