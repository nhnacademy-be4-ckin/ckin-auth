package store.ckin.auth.token.service.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.auth.token.service.TokenService;
import store.ckin.auth.token.service.domain.TokenRequestDto;
import store.ckin.auth.token.service.domain.TokenResponseDto;

/**
 * 토큰에 대한 요청을 응답하는 REST Controller 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 27.
 */
@RestController
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    /**
     * Issue token response entity.
     *
     * @param tokenRequestDto the token request
     * @return the response entity
     */
    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDto> issueToken(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
        TokenResponseDto tokenResponseDto = tokenService.issueToken(tokenRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(tokenResponseDto);
    }
}
