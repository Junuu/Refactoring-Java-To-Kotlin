package anthill.Anthill.api.service;

import anthill.Anthill.api.service.JwtService;
import anthill.Anthill.api.service.JwtServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.StringTokenizer;


class JwtServiceImplTest {
    private JwtService jwtService = new JwtServiceImpl();

    @Test
    void 토큰_생성_테스트() {
        //given
        String userId = "testUserId";
        String exceptedToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9";

        //when
        String token = jwtService.create("userId", userId, "access-token");

        StringTokenizer st = new StringTokenizer(token, ".");
        String header = st.nextToken();
        //then
        Assertions.assertThat(header)
                  .isEqualTo(exceptedToken);

    }

    @Test
    void 토큰_유효성_검사_테스트() {
        //given
        String userId = "testUserId";
        String token = jwtService.create("userId", userId, "access-token");

        //when
        boolean result = jwtService.isUsable(token);

        //then
        Assertions.assertThat(result)
                  .isEqualTo(true);
    }

    @Test
    void 토큰_유효하지않음_테스트() {
        //given
        String token = "a.b.c";

        //when
        boolean result = jwtService.isUsable(token);

        //then
        Assertions.assertThat(result)
                  .isEqualTo(false);
    }

}