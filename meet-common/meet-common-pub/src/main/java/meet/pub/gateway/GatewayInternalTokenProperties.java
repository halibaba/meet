package meet.pub.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;

@ConfigurationProperties(prefix = "meet.gateway")
public class GatewayInternalTokenProperties {

    /**
     * 只有网关和 Feign 知道的内部凭证。直连业务服务时必须带上同名请求头。
     */
    private String internalToken;

    public String getInternalToken() {
        return internalToken;
    }

    public void setInternalToken(String internalToken) {
        if (internalToken == null || internalToken.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("meet.gateway.internal-token 至少需要 32 字节");
        }
        this.internalToken = internalToken;
    }
}
