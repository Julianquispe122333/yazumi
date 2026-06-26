package com.example.yazumi.data

object ProductImages {

    private const val LAYS =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fc/Lays_packet.jpg/320px-Lays_packet.jpg"
    private const val LAYS_LIMON =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6a/Lays_Sour_Cream_%26_Onion.jpg/320px-Lays_Sour_Cream_%26_Onion.jpg"
    private const val DORITOS =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Doritos_Nacho_Cheese.jpg/320px-Doritos_Nacho_Cheese.jpg"
    private const val DORITOS_FLAMIN =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8b/Doritos_Flamin%27_Hot.jpg/320px-Doritos_Flamin%27_Hot.jpg"
    private const val CHEETOS =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Cheetos.jpg/320px-Cheetos.jpg"
    private const val CHEETOS_PUFFS =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Cheetos_Puffs.jpg/320px-Cheetos_Puffs.jpg"
    private const val PEANUTS =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Roasted_peanuts.jpg/320px-Roasted_peanuts.jpg"
    private const val CHEESE_BALLS =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3a/Cheese_puffs.jpg/320px-Cheese_puffs.jpg"

    fun forProduct(idProducto: Int, marca: String): String = when (idProducto) {
        1, 3, 4 -> LAYS
        2 -> LAYS_LIMON
        5 -> DORITOS
        6, 7 -> DORITOS_FLAMIN
        8, 10 -> CHEETOS
        9 -> CHEETOS_PUFFS
        11, 12 -> PEANUTS
        13, 14 -> CHEESE_BALLS
        else -> forBrand(marca)
    }

    fun forBrand(marca: String): String = when (marca.lowercase()) {
        "lays" -> LAYS
        "doritos" -> DORITOS
        "cheetos" -> CHEETOS
        "cuates" -> PEANUTS
        "cheese tris" -> CHEESE_BALLS
        else -> LAYS
    }
}
