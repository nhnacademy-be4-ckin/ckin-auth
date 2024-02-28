package store.ckin.auth.filter;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import store.ckin.auth.provider.JwtProvider;

/**
 * JWT 유효성을 검사하고 재발급을 처리하는 Filter 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final JwtProvider jwtProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JwtProvider jwtProvider) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
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

        //TODO: 인증이 되었다면 Refresh Token Rotation 에 의한 재발급

        chain.doFilter(request, response);
    }
}
