package store.ckin.auth.filter;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import store.ckin.auth.provider.JwtProvider;
import store.ckin.auth.token.service.TokenService;
import store.ckin.auth.token.service.domain.TokenRequestDto;
import store.ckin.auth.token.service.domain.TokenResponseDto;

/**
 * JWT 유효성을 검사하고 재발급을 처리하는 Filter 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final JwtProvider jwtProvider;

    private final TokenService tokenService;

    private final RedisTemplate<String, Object> redisTemplate;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JwtProvider jwtProvider,
                                  TokenService tokenService,
                                  RedisTemplate<String, Object> redisTemplate) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.tokenService = tokenService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String refreshToken = request.getHeader("Authorization");

        if (Objects.isNull(refreshToken) || !refreshToken.startsWith(JwtProvider.AUTHORIZATION_SCHEME_BEARER)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            chain.doFilter(request, response);

            return;
        }

        if (!jwtProvider.isValidate(refreshToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            chain.doFilter(request, response);

            return;
        }

        // 인증이 되었다면 Refresh Token Rotation 에 의한 재발급
        String uuid = jwtProvider.resolveToken(refreshToken, "uuid");
        String id = (String) redisTemplate.opsForHash().get(uuid, "id");
        TokenRequestDto tokenRequestDto = new TokenRequestDto(id);
        TokenResponseDto tokenResponseDto = tokenService.issueToken(tokenRequestDto);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("AccessToken", tokenResponseDto.getAccessToken());
        response.setHeader("RefreshToken", tokenResponseDto.getAccessToken());

        chain.doFilter(request, response);
    }
}
