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
        String header = request.getHeader("Authorization");

        if (Objects.isNull(header) || !header.startsWith(JwtProvider.AUTHORIZATION_SCHEME_BEARER)) {
            log.debug("JwtAuthorizationFilter : Invalid header [{}]", header);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            chain.doFilter(request, response);

            return;
        }

        String refreshToken = header.replace(JwtProvider.AUTHORIZATION_SCHEME_BEARER, "");
        log.debug("Token : {}", refreshToken);

        if (!jwtProvider.isValidate(refreshToken)) {
            log.debug("JwtAuthorizationFilter : Refresh Token [{}] is not validate", refreshToken);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            chain.doFilter(request, response);

            return;
        }

        chain.doFilter(request, response);
    }
}
