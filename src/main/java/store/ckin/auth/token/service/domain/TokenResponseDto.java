package store.ckin.auth.token.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토큰 요청에 대한 응답 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 28.
 */
@Getter
@AllArgsConstructor
public class TokenResponseDto {
    String accessToken;

    String refreshToken;
}
