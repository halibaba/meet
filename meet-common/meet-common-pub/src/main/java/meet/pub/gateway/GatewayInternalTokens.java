package meet.pub.gateway;

/**
 * 网关转发给业务服务时携带的内部凭证。
 */
public final class GatewayInternalTokens {

    public static final String HEADER = "X-Gateway-Token";

    private GatewayInternalTokens() {
    }
}
