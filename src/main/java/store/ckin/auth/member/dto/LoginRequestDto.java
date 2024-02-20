package store.ckin.auth.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청을 위한 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 19.
 */
@Getter
@NoArgsConstructor
public class LoginRequestDto {
    @Email
    @NotBlank
    private String email;
}
