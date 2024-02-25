package store.ckin.auth.token.service.impl;

import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import store.ckin.auth.provider.JwtProvider;
import store.ckin.auth.token.service.TokenService;

/**
 * TokenService 의 구현체 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 23.
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtProvider jwtProvider;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void issueToken(HttpServletResponse response, Authentication authResult) {
        String accessToken = jwtProvider.createAccessToken(authResult);
        String refreshToken = jwtProvider.createRefreshToken(authResult);

        response.setHeader("Authorization", accessToken);

        redisTemplate.opsForValue()
                .set(authResult.getName(), refreshToken, JwtProvider.REFRESH_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }
}
