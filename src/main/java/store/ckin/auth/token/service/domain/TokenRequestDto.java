package store.ckin.auth.token.service.domain;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 인증된 유저의 토큰을 요청하는 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 27.
 */
@Getter
@NoArgsConstructor
public class TokenRequestDto {
    @NotBlank
    String id;
}
