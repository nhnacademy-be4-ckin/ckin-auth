package store.ckin.auth.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.time.Duration;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public static final long ACCESS_EXPIRATION_TIME = Duration.ofMinutes(30).toMillis();

    public static final long REFRESH_EXPIRATION_TIME = Duration.ofHours(2).toMillis();

    public static final String AUTHORIZATION_SCHEME_BEARER = "Bearer ";

    /**
     * JWT Access Token 을 생성하는 메서드 입니다.
     *
     * @return JWT Access Token
     */
    private static String createToken(String uuid, String tokenType, Long expirationTime) {
        Date now = new Date();

        return JWT.create()
                .withSubject(tokenType)
                .withClaim("uuid", uuid)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + expirationTime))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public static String createAccessToken(String uuid) {
        return createToken(uuid, ACCESS_TOKEN_SUBJECT, ACCESS_EXPIRATION_TIME);
    }

    public static String createRefreshToken(String uuid) {
        return createToken(uuid, REFRESH_TOKEN_SUBJECT, REFRESH_EXPIRATION_TIME);
    }

    /**
     * 토큰 유효성을 검증하는 메서드 입니다.
     *
     * @param token Token
     * @return 유효성 검증 여부
     */
    public static boolean isValidate(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            log.error("{} :  Token Validation failed", ex.getClass().getName());
            return false;
        }
    }

    /**
     * 토큰에서 Claim 값을 가져오는 메서드 입니다.
     *
     * @param token Token
     * @param name  Claim key
     * @return Claim Value
     */
    public static String resolveToken(String token, String name) {
        return JWT.decode(token)
                .getClaim(name)
                .asString();
    }
}
