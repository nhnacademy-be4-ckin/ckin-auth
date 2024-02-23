package store.ckin.auth.member.adapter;

import java.util.Optional;
import store.ckin.auth.member.dto.MemberAuthRequestDto;
import store.ckin.auth.member.dto.MemberAuthResponseDto;

/**
 * Member 에 관한 Auth 를 처리하는 Adapter interface 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 19.
 */
public interface MemberAuthAdapter {
    Optional<MemberAuthResponseDto> getLoginInfo(MemberAuthRequestDto memberAuthRequestDto);
}
