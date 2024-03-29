package store.ckin.auth.token.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.auth.token.domain.TokenRequestDto;
import store.ckin.auth.token.domain.TokenResponseDto;
import store.ckin.auth.token.service.TokenService;

/**
 * 토큰에 대한 요청을 응답하는 REST Controller 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 27.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    /**
     * 로그인 시 토큰을 발급하는 메서드 입니다.
     *
     * @param tokenRequestDto 인증된 유저의 토큰을 요청하는 DTO 입니다
     * @return AccessToken, RefreshToken
     */
    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDto> issueToken(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
        TokenResponseDto tokenResponseDto = tokenService.issueToken(tokenRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(tokenResponseDto);
    }

    /**
     * 토큰을 재발급 하는 메서드 입니다.
     *
     * @param refreshToken 헤더에 등록된 refreshToken 입니다.
     * @return 재발급된 AccessToken, RefreshToken
     */
    @PostMapping("/auth/reissue")
    public ResponseEntity<TokenResponseDto> reissueToken(@RequestHeader("Authorization") String refreshToken) {
        TokenResponseDto tokenResponseDto = tokenService.reissueToken(refreshToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("AccessToken", tokenResponseDto.getAccessToken())
                .header("RefreshToken", tokenResponseDto.getRefreshToken())
                .build();
    }
}
