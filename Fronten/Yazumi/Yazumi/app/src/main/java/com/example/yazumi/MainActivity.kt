package com.example.yazumi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.yazumi.ui.navigation.YazumiNavHost
import com.example.yazumi.ui.theme.YazumiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as YazumiApplication).container
        setContent {
            YazumiTheme {
                YazumiNavHost(container = container)
            }
        }
    }
}
