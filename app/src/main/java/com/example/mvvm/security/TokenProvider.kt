package com.example.mvvm.security

interface TokenProvider {
    fun getToken(): String?
}
