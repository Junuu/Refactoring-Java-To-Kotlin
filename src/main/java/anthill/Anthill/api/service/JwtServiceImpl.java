package anthill.Anthill.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private static final String Secret_KEY = "your_secret_key";
    private static final int EXPIRE_MINUTES = 60;

    @Override
    public String create(String key, String data, String subject) {
        String jwt = Jwts.builder()
                         .setHeaderParam("typ", "JWT")
                         //.setHeaderParam("regDate", System.currentTimeMillis())
                         .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * EXPIRE_MINUTES))
                         .setSubject(subject)
                         .claim(key, data)
                         .signWith(SignatureAlgorithm.HS256, this.generateKey())
                         .compact();
        return jwt;
    }

    @Override
    public boolean isUsable(String jwt) {
        try {
            Jws<Claims> claims = Jwts.parser()
                                     .setSigningKey(this.generateKey())
                                     .parseClaimsJws(jwt);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public byte[] generateKey() {
        byte[] key = null;
        try {
            key = Secret_KEY.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            if (log.isInfoEnabled()) {
                e.printStackTrace();
            } else {
                log.error("Making JWT Key Error ::: {}", e.getMessage());
            }
        }

        return key;
    }
}
