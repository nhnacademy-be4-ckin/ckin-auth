package store.ckin.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import store.ckin.auth.filter.JwtAuthenticationFilter;
import store.ckin.auth.member.provider.MemberAuthenticationProvider;
import store.ckin.auth.member.service.MemberDetailsService;
import store.ckin.auth.util.JwtProvider;

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
    private final MemberDetailsService memberDetailsService;

    private final JwtProvider jwtProvider;

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
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    /**
     * JwtAuthenticationFilter 를 Bean 으로 등록.
     *
     * @return JwtAuthenticationFilter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter =  new JwtAuthenticationFilter(jwtProvider);
        filter.setAuthenticationManager(authenticationManager(null));
        filter.setUsernameParameter("email");
        filter.setPasswordParameter("password");

        return filter;
    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public MemberAuthenticationProvider memberAuthenticationProvider() {
        return new MemberAuthenticationProvider(memberDetailsService, bcryptPasswordEncoder());
    }
}
