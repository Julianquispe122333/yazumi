package com.example.yazumi.ui.util

import java.util.Locale

fun formatSoles(amount: Double): String =
    "S/ ${String.format(Locale("es", "PE"), "%.2f", amount)}"
