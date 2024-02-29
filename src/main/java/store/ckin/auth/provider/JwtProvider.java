package store.ckin.auth.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.time.Duration;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Member 정보로 JWT 토큰을 생성, 관리하는 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 23.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {
    public static final String SECRET_KEY = "ckin";

    public static final String ACCESS_TOKEN_SUBJECT = "ckin_access_token";

    public static final String REFRESH_TOKEN_SUBJECT = "ckin_refresh_token";

    public static final long ACCESS_EXPIRATION_TIME = Duration.ofSeconds(30).toMillis();

    public static final long REFRESH_EXPIRATION_TIME = Duration.ofMinutes(3).toMillis();

    public static final String AUTHORIZATION_SCHEME_BEARER = "Bearer ";

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * JWT Access Token 을 생성하는 메서드 입니다.
     *
     * @return JWT Access Token
     */
    private String createToken(String uuid, String tokenType, Long expirationTime) {
        Date now = new Date();

        return AUTHORIZATION_SCHEME_BEARER
                + JWT.create()
                .withSubject(tokenType)
                .withClaim("uuid", uuid)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + expirationTime))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String createAccessToken(String uuid) {
        return createToken(uuid, ACCESS_TOKEN_SUBJECT, ACCESS_EXPIRATION_TIME);
    }

    public String createRefreshToken(String uuid) {
        return createToken(uuid, REFRESH_TOKEN_SUBJECT, REFRESH_EXPIRATION_TIME);
    }

    /**
     * 토큰 유효성을 검증하는 메서드 입니다.
     *
     * @param token Token
     * @return 유효성 검증 여부
     */
    public boolean isValidate(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            log.error("{} :  Token Validation failed", ex.getClass().getName());
            return false;
        }
    }

    public String resolveToken(String token, String name) {
        return JWT.decode(token)
                .getClaim(name)
                .asString();
    }
}
