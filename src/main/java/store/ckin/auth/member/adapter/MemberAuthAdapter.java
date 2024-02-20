package store.ckin.auth.member.adapter;

import store.ckin.auth.member.dto.MemberInfoRequestDto;
import store.ckin.auth.member.dto.MemberInfoResponseDto;

/**
 * Member 에 관한 Auth 를 처리하는 Adapter interface 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 19.
 */
public interface MemberAuthAdapter {
    MemberInfoResponseDto getLoginInfo(MemberInfoRequestDto memberInfoRequestDto);
}
