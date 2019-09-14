package dst.abc_bg.config;

import dst.abc_bg.areas.user.interceptors.DenyAuthenticatedAccessOnFreeAccessPagesInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private static final String LOGIN = "/users/login";
    private static final String REGISTER = "/users/register";

    private final DenyAuthenticatedAccessOnFreeAccessPagesInterceptor denyAuthenticatedAccessOnFreeAccessPagesInterceptor;

    @Autowired
    public WebMvcConfiguration(DenyAuthenticatedAccessOnFreeAccessPagesInterceptor denyAuthenticatedAccessOnFreeAccessPagesInterceptor) {
        this.denyAuthenticatedAccessOnFreeAccessPagesInterceptor = denyAuthenticatedAccessOnFreeAccessPagesInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.denyAuthenticatedAccessOnFreeAccessPagesInterceptor).addPathPatterns(LOGIN, REGISTER);
    }
}
