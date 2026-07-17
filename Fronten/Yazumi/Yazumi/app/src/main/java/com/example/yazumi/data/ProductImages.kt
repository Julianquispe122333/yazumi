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

    private const val RUFFLES =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/Ruffles_potato_chips_bag.jpg/320px-Ruffles_potato_chips_bag.jpg"
    private const val NATUCHIPS =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Plantain_chips_plate.jpg/320px-Plantain_chips_plate.jpg"
    private const val TORTEES =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Tortilla_chips_with_chili.jpg/320px-Tortilla_chips_with_chili.jpg"

    fun forProduct(idProducto: Int, marca: String): String = forBrand(marca)

    fun forBrand(marca: String): String = when (marca.lowercase()) {
        "lays" -> LAYS
        "doritos" -> DORITOS
        "cheetos" -> CHEETOS
        "cuates" -> PEANUTS
        "cheese tris" -> CHEESE_BALLS
        "ruffles" -> RUFFLES
        "karinto" -> PEANUTS
        "natuchips" -> NATUCHIPS
        "tortees" -> TORTEES
        else -> LAYS
    }
}

