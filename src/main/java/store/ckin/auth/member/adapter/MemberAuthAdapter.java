package store.ckin.auth.member.adapter;

import store.ckin.auth.member.dto.LoginRequestDto;
import store.ckin.auth.member.dto.LoginResponseDto;

/**
 * Member 에 관한 Auth 를 처리하는 Adapter interface 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 19.
 */
public interface MemberAuthAdapter {
    LoginResponseDto getLoginInfo(LoginRequestDto loginRequestDto);
}
