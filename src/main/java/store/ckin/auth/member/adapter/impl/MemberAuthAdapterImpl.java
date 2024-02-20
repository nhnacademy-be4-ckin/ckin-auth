package store.ckin.auth.member.adapter.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import store.ckin.auth.config.ServerPortProperties;
import store.ckin.auth.member.adapter.MemberAuthAdapter;
import store.ckin.auth.member.dto.LoginRequestDto;
import store.ckin.auth.member.dto.LoginResponseDto;

/**
 * MemberAuthAdapter 의 구현체 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 19.
 */
@Component
@RequiredArgsConstructor
public class MemberAuthAdapterImpl implements MemberAuthAdapter {
    private final RestTemplate restTemplate;

    private final ServerPortProperties serverPortProperties;

    @Override
    public LoginResponseDto getLoginInfo(LoginRequestDto loginRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<LoginRequestDto> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<LoginResponseDto> exchange = restTemplate.exchange(
                serverPortProperties.getApiUri() + "/auth/login",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        return exchange.getBody();
    }
}
