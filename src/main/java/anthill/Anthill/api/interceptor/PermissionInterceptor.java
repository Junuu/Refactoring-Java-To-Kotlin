package anthill.Anthill.api.interceptor;

import anthill.Anthill.api.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {

    final JwtService jwtService;
    final static String GET = "GET";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final String method = request.getMethod();

        if (method.equals(GET)) {
            return true;
        }

        String token = request.getHeader("access-token");
        if (!jwtService.isUsable(token)) {
            throw new AuthenticationException("토큰이 유효하지 않습니다.");
        }
        return true;

    }
}
