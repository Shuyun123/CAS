package net.anumbrella.sso.config;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Anumbrella
 */
@Configuration
@Component
public class CasCustomConfig {

    @Autowired
    SpringCasAutoconfig autoconfig;

    private static boolean casEnabled = true;

    public CasCustomConfig() {
    }

    @Bean
    public SpringCasAutoconfig getSpringCasAutoconfig() {
        return new SpringCasAutoconfig();
    }

    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<SingleSignOutHttpSessionListener>();
        listener.setEnabled(casEnabled);
        listener.setListener(new SingleSignOutHttpSessionListener());
        listener.setOrder(1);
        return listener;
    }

    /**
     * 该过滤器用于实现单点登出功能，单点退出配置，一定要放在其他filter之前
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new SingleSignOutFilter());
        filterRegistration.setEnabled(casEnabled);
        if (autoconfig.getSignOutFilters().size() > 0) {
            filterRegistration.setUrlPatterns(autoconfig.getSignOutFilters());
        } else {
            filterRegistration.addUrlPatterns("/*");
        }
        filterRegistration.addInitParameter("casServerUrlPrefix", autoconfig.getCasServerUrlPrefix());
        filterRegistration.setOrder(3);
        return filterRegistration;
    }

    /**
     * 该过滤器负责用户的认证工作
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean authenticationFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new AuthenticationFilter());
        filterRegistration.setEnabled(casEnabled);
        if (autoconfig.getAuthFilters().size() > 0) {
            filterRegistration.setUrlPatterns(autoconfig.getAuthFilters());
        } else {
            filterRegistration.addUrlPatterns("/*");
        }
        if (autoconfig.getIgnoreFilters() != null) {
            filterRegistration.addInitParameter("ignorePattern", autoconfig.getIgnoreFilters());
        }
        filterRegistration.addInitParameter("casServerLoginUrl", autoconfig.getCasServerLoginUrl());
        filterRegistration.addInitParameter("serverName", autoconfig.getServerName());
        filterRegistration.addInitParameter("useSession", autoconfig.isUseSession() ? "true" : "false");
        filterRegistration.addInitParameter("redirectAfterValidation", autoconfig.isRedirectAfterValidation() ? "true" : "false");
        filterRegistration.setOrder(4);
        return filterRegistration;
    }

    /**
     * 该过滤器负责对Ticket的校验工作,使用CAS 3.0协议
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean cas30ProxyReceivingTicketValidationFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
        filterRegistration.setEnabled(casEnabled);
        if (autoconfig.getValidateFilters().size() > 0) {
            filterRegistration.setUrlPatterns(autoconfig.getValidateFilters());
        } else {
            filterRegistration.addUrlPatterns("/*");
        }
        filterRegistration.addInitParameter("casServerUrlPrefix", autoconfig.getCasServerUrlPrefix());
        filterRegistration.addInitParameter("serverName", autoconfig.getServerName());
        filterRegistration.setOrder(5);
        return filterRegistration;
    }

    @Bean
    public FilterRegistrationBean httpServletRequestWrapperFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new HttpServletRequestWrapperFilter());
        filterRegistration.setEnabled(true);
        if (autoconfig.getRequestWrapperFilters().size() > 0) {
            filterRegistration.setUrlPatterns(autoconfig.getRequestWrapperFilters());
        } else {
            filterRegistration.addUrlPatterns("/*");
        }
        filterRegistration.setOrder(6);
        return filterRegistration;
    }

    /**
     * 该过滤器使得可以通过org.jasig.cas.client.util.AssertionHolder来获取用户的登录名。
     * 比如AssertionHolder.getAssertion().getPrincipal().getName()。
     * 这个类把Assertion信息放在ThreadLocal变量中，这样应用程序不在web层也能够获取到当前登录信息
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean assertionThreadLocalFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new AssertionThreadLocalFilter());
        filterRegistration.setEnabled(true);
        if (autoconfig.getAssertionFilters().size() > 0) {
            filterRegistration.setUrlPatterns(autoconfig.getAssertionFilters());
        } else {
            filterRegistration.addUrlPatterns("/*");
        }
        filterRegistration.setOrder(7);
        return filterRegistration;
    }

}
