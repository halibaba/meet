package meet.pub.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meet.pub.entity.R;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 拒绝没有网关内部凭证的请求，避免绕过网关直接访问服务端口。
 */
public class GatewayInternalTokenFilter extends OncePerRequestFilter {

    private final String expectedToken;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GatewayInternalTokenFilter(String expectedToken) {
        this.expectedToken = expectedToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String actualToken = request.getHeader(GatewayInternalTokens.HEADER);
        if (matches(expectedToken, actualToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), R.error().code(403).message("只能通过网关访问"));
    }

    private boolean matches(String expected, String actual) {
        if (expected == null || actual == null) {
            return false;
        }
        return MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8), actual.getBytes(StandardCharsets.UTF_8));
    }
}
