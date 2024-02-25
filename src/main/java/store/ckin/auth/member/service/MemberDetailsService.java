package store.ckin.auth.member.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import store.ckin.auth.member.adapter.MemberAuthAdapter;
import store.ckin.auth.member.dto.MemberAuthRequestDto;
import store.ckin.auth.member.dto.MemberAuthResponseDto;

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
        MemberAuthRequestDto memberAuthRequestDto = new MemberAuthRequestDto(email);
        Optional<MemberAuthResponseDto> responseDto = memberAuthAdapter.getLoginInfo(memberAuthRequestDto);
        MemberAuthResponseDto authInfo;

        if (responseDto.isPresent()) {
            authInfo = responseDto.get();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(authInfo::getRole);

            return new User(authInfo.getId().toString(), authInfo.getPassword(), authorities);
        }

        throw new UsernameNotFoundException(email + " : email 에 대한 정보가 없어 인증에 실패 하였습니다.");
    }
}
