package dst.abc_bg.areas.user.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class DenyAuthenticatedAccessOnFreeAccessPagesInterceptor extends HandlerInterceptorAdapter {
    private static final String HOME = "/";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getUserPrincipal() != null) {
            response.sendRedirect(HOME);
        }

        return super.preHandle(request, response, handler);
    }
}
