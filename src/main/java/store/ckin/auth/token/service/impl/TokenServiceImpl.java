package store.ckin.auth.token.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import store.ckin.auth.provider.JwtProvider;
import store.ckin.auth.token.service.TokenService;
import store.ckin.auth.token.service.domain.TokenRequestDto;
import store.ckin.auth.token.service.domain.TokenResponseDto;

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

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public TokenResponseDto issueToken(TokenRequestDto tokenRequestDto) {
        String uuid = UUID.randomUUID().toString();
        String refreshToken = jwtProvider.createRefreshToken(uuid);

        redisTemplate.opsForHash()
                .put(uuid, JwtProvider.REFRESH_TOKEN_SUBJECT, refreshToken);

        String id = tokenRequestDto.getId();

        redisTemplate.opsForHash()
                .put(uuid, "id", id);
        redisTemplate.expire(uuid, JwtProvider.REFRESH_EXPIRATION_TIME, TimeUnit.MILLISECONDS);

        String accessToken = jwtProvider.createAccessToken(uuid);

        return new TokenResponseDto(accessToken, refreshToken);
    }
}
