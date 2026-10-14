package com.example.horariosurquiza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.horariosurquiza.ui.theme.HorariosUrquizaTheme

class MainActivity : ComponentActivity() {

    private val viewModel: HorariosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HorariosUrquizaTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
