package store.ckin.auth.member.service;

import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import store.ckin.auth.member.adapter.MemberAuthAdapter;
import store.ckin.auth.member.dto.MemberAuthRequestDto;
import store.ckin.auth.member.dto.MemberAuthResponseDto;
import store.ckin.auth.member.exception.ServerErrorException;

/**
 * 인증된 멤버의 정보를 관리하는 Service 클래스 입니다.
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
        try {
            MemberAuthResponseDto memberInfo =
                    memberAuthAdapter.getLoginInfo(new MemberAuthRequestDto(email));

            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(memberInfo::getRole);

            return new User(memberInfo.getId().toString(), memberInfo.getPassword(), authorities);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new UsernameNotFoundException(
                        String.format("Not found information for this email [%s]", email));
            } else {
                throw new ServerErrorException();
            }
        } catch (HttpServerErrorException ex) {
            throw new ServerErrorException();
        }

    }
}
