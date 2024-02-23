package store.ckin.auth.token.service;

import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

/**
 * Token 관리 로직을 처리하는 Service interface 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 23.
 */
public interface TokenService {
    void issueToken(HttpServletResponse response, Authentication authResult);
}
