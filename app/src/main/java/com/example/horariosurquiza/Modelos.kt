package com.example.horariosurquiza

// Data class para representar cada tren
data class Tren(
    val horaLlegadaOrigen: String,
    val horaLlegadaDestino: String,
    val tiempoRestanteMinutos: Int
)

// Data class para representar los feriados cargados desde Firestore
data class Feriado(
    val fecha: String,
    val nombre: String,
    val tipo: String = ""
)

// Data class para representar el estado de actualización de la app
data class EstadoActualizacion(
    val hayActualizacion: Boolean = false,
    val urlDescarga: String = "",
    val versionNueva: String = ""
)

// Estados de pantalla para la navegación animada
enum class Pantalla {
    BUSQUEDA,
    RESULTADOS
}

// Compara versiones semánticas ("1.0", "1.1.2") para determinar si remote es mayor que installed
fun esVersionMasNueva(installed: String, remote: String): Boolean {
    return try {
        val partsInstalled = installed.split(".").map { it.trim().toIntOrNull() ?: 0 }
        val partsRemote = remote.split(".").map { it.trim().toIntOrNull() ?: 0 }

        val maxParts = maxOf(partsInstalled.size, partsRemote.size)
        for (i in 0 until maxParts) {
            val vInstalled = partsInstalled.getOrElse(i) { 0 }
            val vRemote = partsRemote.getOrElse(i) { 0 }
            if (vRemote > vInstalled) return true
            if (vRemote < vInstalled) return false
        }
        false
    } catch (_: Exception) {
        remote != installed && remote.isNotEmpty()
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

// Función auxiliar para formatear los minutos restantes manteniendo siempre "HH:MM hs (En X min)"
fun formatearTiempoRestante(horaStr: String, diffMins: Int): String {
    return if (diffMins > 0) {
        "$horaStr hs (En $diffMins min)"
    } else if (diffMins == 0) {
        "$horaStr hs (Ahora)"
    } else {
        val diffPositiva = diffMins + 1440
        "$horaStr hs (En $diffPositiva min)"
    }
}
