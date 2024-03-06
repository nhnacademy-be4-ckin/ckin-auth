package store.ckin.auth.token.service;

import store.ckin.auth.token.service.domain.TokenRequestDto;
import store.ckin.auth.token.service.domain.TokenResponseDto;

/**
 * Token 관리 로직을 처리하는 Service interface 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 23.
 */
public interface TokenService {
    TokenResponseDto issueToken(TokenRequestDto tokenRequestDto);
}
