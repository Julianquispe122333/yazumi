package com.example.yazumi.ui.navigation

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

    fun category(marca: String) = "category/$marca"
    fun product(id: Int) = "product/$id"
}
