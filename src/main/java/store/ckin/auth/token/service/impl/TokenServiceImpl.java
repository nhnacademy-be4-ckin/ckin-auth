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
        String authority = tokenRequestDto.getAuthority();

        redisTemplate.opsForHash()
                .put(uuid, "id", id);
        redisTemplate.opsForHash()
                .put(uuid, "authority", authority);
        redisTemplate.expire(uuid, JwtProvider.REFRESH_EXPIRATION_TIME, TimeUnit.MILLISECONDS);

        String accessToken = JwtProvider.createAccessToken(uuid);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    @Override
    public TokenResponseDto reissueToken(String refreshToken) {
        String jwt = refreshToken.replace(JwtProvider.AUTHORIZATION_SCHEME_BEARER, "");
        String uuid = JwtProvider.resolveToken(jwt, "uuid");
        String id = (String) redisTemplate.opsForHash().get(uuid, "id");
        String authority = (String) redisTemplate.opsForHash().get(uuid, "authority");

        return issueToken(new TokenRequestDto(id, authority));
    }
}
