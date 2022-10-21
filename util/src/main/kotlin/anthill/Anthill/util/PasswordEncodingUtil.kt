package anthill.Anthill.util

import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class PasswordEncodingUtil {
    fun hashingPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun checkPassword(inputPassword: String, savedPassword: String): Boolean{
        return BCrypt.checkpw(inputPassword,savedPassword)
    }
}