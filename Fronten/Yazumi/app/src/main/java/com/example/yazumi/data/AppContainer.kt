package com.example.yazumi.data

import android.content.Context
import com.example.yazumi.data.local.SessionManager
import com.example.yazumi.data.remote.ApiClient
import com.example.yazumi.data.repository.AuthRepository
import com.example.yazumi.data.repository.CartRepository
import com.example.yazumi.data.repository.OrderRepository
import com.example.yazumi.data.repository.ProductRepository

class AppContainer(context: Context) {
    private val sessionManager = SessionManager(context)
    private val api = ApiClient.create()

    val authRepository = AuthRepository(api, sessionManager)
    val productRepository = ProductRepository(api)
    val cartRepository = CartRepository(api, sessionManager)
    val orderRepository = OrderRepository(api, sessionManager)
}
