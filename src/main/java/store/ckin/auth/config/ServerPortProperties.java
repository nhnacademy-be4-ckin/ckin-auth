package store.ckin.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 서버들의 포트 번호를 관리하기 위한 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 19.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "port")
public class ServerPortProperties {
    private String gatewayUri;

    private String apiUri;
}
