package anthill.Anthill.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException
import java.util.*


@Component
class JwtUtil {
    fun create(key: String, data: String, subject: String): String {
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * EXPIRE_MINUTES))
            .setSubject(subject)
            .claim(key, data)
            .signWith(SignatureAlgorithm.HS256, this.generateKey())
            .compact()
    }

    fun isUsable(jwt: String): Boolean {
        return try {
            Jwts.parser()
                .setSigningKey(this.generateKey())
                .parseClaimsJws(jwt)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun generateKey(): ByteArray {
        lateinit var key: ByteArray
        try {
            key = SECRET_KEY.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
        }
        return key
    }

    companion object {
        const val SECRET_KEY = "your_secret_key"
        const val EXPIRE_MINUTES = 60
    }
}