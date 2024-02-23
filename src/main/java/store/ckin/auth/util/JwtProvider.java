package store.ckin.auth.util;

import java.time.Duration;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Member 정보로 JWT 토큰을 생성, 관리하는 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 23.
 */
@Component
@RequiredArgsConstructor
public class JwtProvider {
    private static final String SECRET_KEY = "ckin";

    private static final String ACCESS_TOKEN_SUBJECT = "ckin_access_token";

    private static final String REFRESH_TOKEN_SUBJECT = "ckin_refresh_token";

    private static final long ACCESS_EXPIRATION_TIME = Duration.ofHours(1).toMillis();

    private static final long REFRESH_EXPIRATION_TIME = Duration.ofDays(2).toMillis();

    private static final String AUTHORIZATION_SCHEME_BEARER = "Bearer ";

    private final RedisTemplate<String, String> redisTemplate;

    private final UserDetailsService memberDetailsService;

    /**
     * JWT Access Token 을 생성하는 메서드 입니다.
     *
     * @param authentication 인증된 계정의 정보를 담은 Authentication
     * @return JWT Access Token
     */
    private String createToken(Authentication authentication, String tokenType, Long expirationTime) {
        Long id = Long.parseLong(authentication.getName());
        String authority = authentication.getAuthorities().toString();
        Date now = new Date();

        return JWT.create()
                .withSubject(tokenType)
                .withClaim("id", id)
                .withClaim("authority", authority)
                .withExpiresAt(new Date(now.getTime() + expirationTime))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String createAccessToken(Authentication authentication) {
        return AUTHORIZATION_SCHEME_BEARER
                + createToken(authentication, ACCESS_TOKEN_SUBJECT, ACCESS_EXPIRATION_TIME);
    }

    public String createRefreshToken(Authentication authentication) {
        return AUTHORIZATION_SCHEME_BEARER
                + createToken(authentication, REFRESH_TOKEN_SUBJECT, REFRESH_EXPIRATION_TIME);
    }
}
