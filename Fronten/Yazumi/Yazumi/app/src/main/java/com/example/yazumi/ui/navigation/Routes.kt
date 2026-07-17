package com.example.yazumi.ui.navigation

import android.net.Uri

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val CATALOG = "catalog"
    const val CATEGORY = "category/{marca}"
    const val PRODUCT = "product/{id}"
    const val CART = "cart"
    const val ORDERS = "orders"
    const val ADMIN_DASHBOARD = "admin_dashboard"

    /**
     * URL-encodes the category name so that names with spaces or
     * special characters (e.g. "Piqueo Snax", "Doritos Flamin Hot")
     * don't break the navigation route matching.
     */
    fun category(marca: String) = "category/${Uri.encode(marca)}"
    fun product(id: Int) = "product/$id"
}
