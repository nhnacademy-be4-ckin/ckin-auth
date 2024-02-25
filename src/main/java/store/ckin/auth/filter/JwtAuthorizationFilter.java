package store.ckin.auth.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import store.ckin.auth.member.service.MemberDetailsService;
import store.ckin.auth.provider.JwtProvider;

/**
 * JWT 를 이용하여 Member 인가를 처리하는 Filter 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final MemberDetailsService memberDetailsService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  MemberDetailsService memberDetailsService) {
        super(authenticationManager);
        this.memberDetailsService = memberDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String jwtHeader = request.getHeader("Authorization");

        if (Objects.isNull(jwtHeader) || !jwtHeader.startsWith(JwtProvider.AUTHORIZATION_SCHEME_BEARER)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return;
        }

        String jwt = jwtHeader.replace(JwtProvider.AUTHORIZATION_SCHEME_BEARER, "");

        Long id = JWT.require(Algorithm.HMAC512(JwtProvider.SECRET_KEY))
                .build()
                .verify(jwt)
                .getClaim("id")
                .asLong();

        String email = request.getParameter("email");

        if (Objects.isNull(id) || Objects.isNull(email)) {
            return;
        }

        try {
            UserDetails memberDetails = memberDetailsService.loadUserByUsername(email);

            if (!memberDetails.getUsername().equals(id.toString())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                return;
            }

            chain.doFilter(request, response);
        } catch (UsernameNotFoundException ex) {
            log.error("JwtAuthorizationFilter : Not found information matching your email");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
