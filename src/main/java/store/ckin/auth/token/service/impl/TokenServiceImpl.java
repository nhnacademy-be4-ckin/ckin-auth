package store.ckin.auth.token.service.impl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import store.ckin.auth.provider.JwtProvider;
import store.ckin.auth.token.domain.TokenRequestDto;
import store.ckin.auth.token.domain.TokenResponseDto;
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
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public TokenResponseDto issueToken(TokenRequestDto tokenRequestDto) {
        String uuid = UUID.randomUUID().toString();
        String refreshToken = JwtProvider.createRefreshToken(uuid);

        redisTemplate.opsForHash()
                .put(uuid, JwtProvider.REFRESH_TOKEN_SUBJECT, refreshToken);

        String id = tokenRequestDto.getId();

        redisTemplate.opsForHash()
                .put(uuid, "id", id);
        redisTemplate.expire(uuid, JwtProvider.REFRESH_EXPIRATION_TIME, TimeUnit.MILLISECONDS);

        String accessToken = JwtProvider.createAccessToken(uuid);

        return new TokenResponseDto(accessToken, refreshToken);
    }
}
