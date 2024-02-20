package store.ckin.auth.member.service;

import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import store.ckin.auth.member.adapter.MemberAuthAdapter;
import store.ckin.auth.member.dto.LoginRequestDto;
import store.ckin.auth.member.dto.LoginResponseDto;

/**
 * PrincipalService.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 19.
 */
@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberAuthAdapter memberAuthAdapter;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LoginRequestDto loginRequestDto = new LoginRequestDto(email);
        LoginResponseDto responseDto = memberAuthAdapter.getLoginInfo(loginRequestDto);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(responseDto::getRole);

        return new User(responseDto.getEmail(), responseDto.getPassword(), authorities);
    }
}
