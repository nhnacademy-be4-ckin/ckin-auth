package store.ckin.auth.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 로그인 요청 시 Auth Server 에 요청온 계정 정보를 인증하는 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 23.
 */
@RequiredArgsConstructor
public class MemberAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService memberDetailsService;

    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 인증 전의 Authentication 객체
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        String email = token.getName();
        String password = (String) token.getCredentials();

        UserDetails userDetails = memberDetailsService.loadUserByUsername(email);

        if (!bcryptPasswordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException(userDetails.getUsername() + " : Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
