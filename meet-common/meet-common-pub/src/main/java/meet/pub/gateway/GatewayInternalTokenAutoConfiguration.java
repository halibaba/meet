package meet.pub.gateway;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

@Configuration
@EnableConfigurationProperties(GatewayInternalTokenProperties.class)
@ConditionalOnProperty(prefix = "meet.gateway", name = "internal-token")
@Import(GatewayInternalTokenFeignConfiguration.class)
public class GatewayInternalTokenAutoConfiguration {

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FilterRegistrationBean<GatewayInternalTokenFilter> gatewayInternalTokenFilter(
            GatewayInternalTokenProperties properties) {
        FilterRegistrationBean<GatewayInternalTokenFilter> registration = new FilterRegistrationBean<GatewayInternalTokenFilter>();
        registration.setFilter(new GatewayInternalTokenFilter(properties.getInternalToken()));
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.setName("gatewayInternalTokenFilter");
        return registration;
    }
}
