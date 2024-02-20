package store.ckin.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import store.ckin.auth.filter.JwtAuthenticationFilter;
import store.ckin.auth.filter.JwtAuthorizationFilter;
import store.ckin.auth.member.adapter.MemberAuthAdapter;

/**
 * Security 설정을 위한 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberAuthAdapter memberAuthAdapter;

    private final AuthenticationConfiguration authenticationConfiguration;

    /**
     * Security Filter 를 설정하는 Bean method 입니다.
     *
     * @param http the http
     * @return the security filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), memberAuthAdapter), BasicAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager()
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
