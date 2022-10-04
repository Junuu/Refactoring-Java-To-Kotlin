package anthill.Anthill.api.interceptor

import anthill.Anthill.api.service.JwtService
import org.apache.tomcat.websocket.AuthenticationException
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class PermissionInterceptor(
    val jwtService: JwtService,
) : HandlerInterceptor {


    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val method = request.method
        if (method == GET) {
            return true
        }
        val token = request.getHeader("access-token")
        if (jwtService.isUsable(token) == false) {
            throw AuthenticationException("토큰이 유효하지 않습니다.")
        }
        return true
    }

    companion object {
        const val GET = "GET"
    }
}