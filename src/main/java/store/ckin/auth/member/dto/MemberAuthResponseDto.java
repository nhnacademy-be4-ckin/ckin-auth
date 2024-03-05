package store.ckin.auth.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 인증 요청에 응답하는 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberAuthResponseDto {
    Long id;

    String email;

    String password;

    String role;
}
