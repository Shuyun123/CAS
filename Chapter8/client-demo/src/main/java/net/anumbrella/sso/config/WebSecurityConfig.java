
package net.anumbrella.sso.config;


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

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        //设定匹配的优先级

        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/login**");

        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            HttpSession session = request.getSession();


            //获取cas给我们传递回来的对象，这个东西放到了session中
            //session的 key是 _const_cas_assertion_
            Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);

            //获取登录用户名
            String loginName = assertion.getPrincipal().getName();
            System.out.printf("登录用户名:%s\r\n", loginName);


            // 判断是否已有该用户登录的session
            if (session.getAttribute(SESSION_KEY) != null) {
                return true;
            }


            // 跳转到登录页
            String url = "/login";
            response.sendRedirect(url);
            return false;
        }
    }
}
