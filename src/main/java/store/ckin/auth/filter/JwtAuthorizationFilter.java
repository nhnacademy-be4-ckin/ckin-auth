package store.ckin.auth.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import store.ckin.auth.member.adapter.MemberAuthAdapter;
import store.ckin.auth.member.dto.MemberInfoRequestDto;
import store.ckin.auth.member.dto.MemberInfoResponseDto;

/**
 * JWT 를 이용하여 Member 인가를 처리하는 Filter 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final MemberAuthAdapter memberAuthAdapter;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberAuthAdapter memberAuthAdapter) {
        super(authenticationManager);
        this.memberAuthAdapter = memberAuthAdapter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String jwtHeader = request.getHeader("Authorization");

        if (Objects.isNull(jwtHeader) || !jwtHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);

            return;
        }

        String jwt = jwtHeader.replace("Bearer ", "");

        String email = JWT.require(Algorithm.HMAC512("ckin"))
                .build()
                .verify(jwt)
                .getClaim("email")
                .asString();

        if (Objects.nonNull(email)) {
            MemberInfoRequestDto requestDto = new MemberInfoRequestDto(email);

            MemberInfoResponseDto responseDto = memberAuthAdapter.getLoginInfo(requestDto);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(responseDto::getRole);

            User user = new User(responseDto.getEmail(), responseDto.getPassword(), authorities);

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}