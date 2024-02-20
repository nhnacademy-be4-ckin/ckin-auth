package store.ckin.auth.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Front Server 에서 보내는 login 요청 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
@Getter
@NoArgsConstructor
public class LoginInfoRequestDto {
    private String email;

    private String password;
}
