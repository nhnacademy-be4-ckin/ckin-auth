package store.ckin.auth.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Login 요청을 받아 응답하는 dto 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
@Getter
@NoArgsConstructor
public class MemberInfoResponseDto {
    String email;

    String password;

    String role;
}
