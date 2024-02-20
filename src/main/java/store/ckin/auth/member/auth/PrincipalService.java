package store.ckin.auth.member.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import store.ckin.auth.member.adapter.MemberAuthAdapter;

/**
 * PrincipalService.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 19.
 */
@Service
@RequiredArgsConstructor
public class PrincipalService implements UserDetailsService {
    private final MemberAuthAdapter memberAuthAdapter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO
        return null;
    }
}
