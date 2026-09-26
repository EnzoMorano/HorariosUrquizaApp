package com.example.horariosurquiza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horariosurquiza.ui.theme.HorariosUrquizaTheme

// Data class para representar cada tren
data class Tren(
    val horaLlegadaOrigen: String,
    val horaLlegadaDestino: String,
    val tiempoRestanteMinutos: Int
)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(modifier: Modifier = Modifier) {
    // Lista de estaciones de ejemplo
    val estaciones = listOf("Federico Lacroze", "Artigas", "Rubén Darío", "General Lemos")

    // Estados para la estación seleccionada y la visibilidad de los menús
    var estacionOrigen by remember { mutableStateOf("") }
    var estacionDestino by remember { mutableStateOf("") }

    var origenExpanded by remember { mutableStateOf(false) }
    var destinoExpanded by remember { mutableStateOf(false) }

    // Estado para guardar la lista de resultados de trenes
    var listaTrenes by remember { mutableStateOf<List<Tren>>(emptyList()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Selector para Estación de Origen
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
                    focusedLabelColor = Color(0xFFFCCD06),
                    focusedBorderColor = Color(0xFFFCCD06),
                ),
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = origenExpanded,
                onDismissRequest = { origenExpanded = false }
            ) {
                estaciones.forEach { estacion ->
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

        // Selector para Estación de Destino
        ExposedDropdownMenuBox(
            expanded = destinoExpanded,
            onExpandedChange = { destinoExpanded = !destinoExpanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            OutlinedTextField(
                value = estacionDestino,
                onValueChange = {},
                readOnly = true,
                label = { Text("Estación de Destino") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = destinoExpanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color(0xFFFCCD06),
                    focusedBorderColor = Color(0xFFFCCD06),
                ),
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = destinoExpanded,
                onDismissRequest = { destinoExpanded = false }
            ) {
                estaciones.forEach { estacion ->
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

        // Botón Buscar horarios
        Button(
            onClick = {
                // Asignamos una lista simulada de trenes al hacer clic
                listaTrenes = listOf(
                    Tren(horaLlegadaOrigen = "14:15", horaLlegadaDestino = "14:45", tiempoRestanteMinutos = 3),
                    Tren(horaLlegadaOrigen = "14:30", horaLlegadaDestino = "15:00", tiempoRestanteMinutos = 18),
                    Tren(horaLlegadaOrigen = "14:47", horaLlegadaDestino = "15:17", tiempoRestanteMinutos = 35)
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFCCD06),
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(56.dp)
        ) {
            Text(
                text = "Buscar horarios",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Lista de resultados mostrada dinámicamente con LazyColumn
        if (listaTrenes.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(listaTrenes) { index, tren ->
                    val esProximo = index == 0
                    val borderContainerColor = if (esProximo) {
                        Color(0xFFFCCD06)
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
                        border = BorderStroke(1.dp, borderContainerColor)
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
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
                                        text = "Sale en ${tren.tiempoRestanteMinutos} min",
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
                                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
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

@Preview(showBackground = true)
@Composable
fun PantallaPrincipalPreview() {
    HorariosUrquizaTheme {
        PantallaPrincipal()
    }
}
