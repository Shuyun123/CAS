
package net.anumbrella.sso.config;


import net.anumbrella.sso.entity.User;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Anumbrella
 */
@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {


    // 更换CAS中的session中的key
    public final static String SESSION_KEY = AbstractCasFilter.CONST_CAS_ASSERTION;

    // 普通登录SESSION
    public final static String SESSION_LOGIN = "SESSION_LOGIN";

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        //设定匹配的优先级

//        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/api/user/login/**");
        addInterceptor.excludePathPatterns("/api/user/caslogin/**");

        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            HttpSession session = request.getSession(false);

            if (session != null) {
                System.out.println("requst path " + request.getServletPath());

                Assertion assertion = (Assertion) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);

                if (assertion != null) {
                    System.out.println("cas user ---------> " + assertion.getPrincipal().getName());
                }

                User value = (User) session.getAttribute(SESSION_LOGIN);

                System.out.println("security session = null ---------> " + (value == null));

                if (value != null) {
                    return true;
                }
            }

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
