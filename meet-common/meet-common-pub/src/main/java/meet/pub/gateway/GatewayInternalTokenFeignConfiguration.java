package meet.pub.gateway;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 服务之间的 Feign 不经过网关，调用前补上同一个内部凭证。
 */
@Configuration
@ConditionalOnClass(RequestInterceptor.class)
public class GatewayInternalTokenFeignConfiguration {

    @Bean
    public RequestInterceptor gatewayInternalTokenFeignInterceptor(GatewayInternalTokenProperties properties) {
        return template -> {
            template.removeHeader(GatewayInternalTokens.HEADER);
            template.header(GatewayInternalTokens.HEADER, properties.getInternalToken());
        };
    }
}
