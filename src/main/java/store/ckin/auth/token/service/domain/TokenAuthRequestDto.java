package store.ckin.auth.token.service.domain;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * JWT 토큰에 대한 인증을 Auth 서버로 요청하는 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 29.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenAuthRequestDto {
    @NotBlank
    String token;
}
