package dst.abc_bg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String STYLES = "/styles/**";
    private static final String SCRIPTS = "/scripts/**";
    private static final String IMAGES = "/images/**";
    private static final String FAVICON = "/favicon.ico";

    private static final String ROOT = "/";
    private static final String ERROR = "/errors/**";
    private static final String LOGIN = "/users/login";
    private static final String REGISTER = "/users/register";
    private static final String LOGOUT = "/users/logout";
    private static final String ADMIN_AUTHORITY = "hasAuthority('ADMIN')";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String REMEMBER_PARAMETER = "remember";
    private static final String REMEMBER_COOKIE_NAME = "rememberMeCookie";
    private static final String REMEMBER_KEY = "48433e39-e610-4a2c-926c-f86d46f5360a";
    private static final Integer TOKEN_VALIDITY_SECONDS = 100;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(STYLES, SCRIPTS, IMAGES);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(LOGIN, REGISTER, ERROR).permitAll()
                //.antMatchers() --> todo admin urls
                //.access(ADMIN_AUTHORITY)
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin().loginPage(LOGIN).permitAll()
                .usernameParameter(USERNAME)
                .passwordParameter(PASSWORD)
                .loginProcessingUrl(LOGIN)
                .defaultSuccessUrl(ROOT)
                .and()
                .rememberMe()
                .rememberMeParameter(REMEMBER_PARAMETER)
                .rememberMeCookieName(REMEMBER_COOKIE_NAME)
                .key(REMEMBER_KEY)
                .tokenValiditySeconds(TOKEN_VALIDITY_SECONDS)
                .and()
                .logout().logoutUrl(LOGOUT)
                .logoutSuccessUrl(LOGIN)
                .permitAll();
    }
}
