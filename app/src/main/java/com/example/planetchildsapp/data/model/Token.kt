package com.example.planetchildsapp.data.model

import android.util.Log
import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val name: String,
    val email: String,
    val role: String
)

fun parseToken(jsonToken: String): Token {
    Log.i("+++", "Начинаем парсить токен")
    val decodedToken = parseJwt(jsonToken)
    val name = decodedToken.getClaim("name").asString()
    val email = decodedToken.getClaim("email").asString()
    val role = decodedToken.getClaim("role").asString()

    if (name != null &&
        email != null &&
        role != null
    ) {
        return Token(
            name = name,
            email = email,
            role = role
        )
    } else {
        throw RuntimeException()
    }
}

private fun parseJwt(token: String): DecodedJWT {
    return try {
        JWT.decode(token)
    } catch (ex: JWTVerificationException) {
        ex.printStackTrace()
        throw ex
    }
}