package store.ckin.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import store.ckin.auth.member.dto.LoginInfoRequestDto;
import store.ckin.auth.token.service.TokenService;


/**
 * JWT 인증을 처리하는 Filter class 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenService tokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.debug("JwtAuthenticationFilter : Try Login");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            LoginInfoRequestDto loginInfoRequestDto =
                    objectMapper.readValue(request.getInputStream(), LoginInfoRequestDto.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginInfoRequestDto.getEmail(),
                    loginInfoRequestDto.getPassword());

            return getAuthenticationManager().authenticate(token);
        } catch (IOException ex) {
            log.error("JwtAuthenticationFilter : LoginInfoRequest parsing error");

            throw new BadCredentialsException("Failed to parse LoginInfoRequest", ex);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        tokenService.issueToken(response, authResult);
    }
}
