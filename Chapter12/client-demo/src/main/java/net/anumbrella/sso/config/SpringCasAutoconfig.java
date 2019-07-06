package net.anumbrella.sso.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 * @author Anumbrella
 */
@ConfigurationProperties(prefix = "spring.cas")
public class SpringCasAutoconfig {

    static final String separator = ",";

    private String validateFilters;
    private String signOutFilters;
    private String authFilters;
    private String assertionFilters;
    private String requestWrapperFilters;
    private String ignoreFilters; //需要放行的url,多个可以使用|分隔，遵循正则

    private String casServerUrlPrefix;
    private String casServerLoginUrl;
    private String serverName;
    private boolean useSession = true;
    private boolean redirectAfterValidation = true;

    public String getIgnoreFilters() {
        return ignoreFilters;
    }

    public void setIgnoreFilters(String ignoreFilters) {
        this.ignoreFilters = ignoreFilters;
    }

    public List<String> getValidateFilters() {
        return Arrays.asList(validateFilters.split(separator));
    }

    public void setValidateFilters(String validateFilters) {
        this.validateFilters = validateFilters;
    }

    public List<String> getSignOutFilters() {
        return Arrays.asList(signOutFilters.split(separator));
    }

    public void setSignOutFilters(String signOutFilters) {
        this.signOutFilters = signOutFilters;
    }

    public List<String> getAuthFilters() {
        return Arrays.asList(authFilters.split(separator));
    }

    public void setAuthFilters(String authFilters) {
        this.authFilters = authFilters;
    }

    public List<String> getAssertionFilters() {
        return Arrays.asList(assertionFilters.split(separator));
    }

    public void setAssertionFilters(String assertionFilters) {
        this.assertionFilters = assertionFilters;
    }

    public List<String> getRequestWrapperFilters() {
        return Arrays.asList(requestWrapperFilters.split(separator));
    }

    public void setRequestWrapperFilters(String requestWrapperFilters) {
        this.requestWrapperFilters = requestWrapperFilters;
    }

    public String getCasServerUrlPrefix() {
        return casServerUrlPrefix;
    }

    public void setCasServerUrlPrefix(String casServerUrlPrefix) {
        this.casServerUrlPrefix = casServerUrlPrefix;
    }

    public String getCasServerLoginUrl() {
        return casServerLoginUrl;
    }

    public void setCasServerLoginUrl(String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public boolean isRedirectAfterValidation() {
        return redirectAfterValidation;
    }

    public void setRedirectAfterValidation(boolean redirectAfterValidation) {
        this.redirectAfterValidation = redirectAfterValidation;
    }

    public boolean isUseSession() {
        return useSession;
    }

    public void setUseSession(boolean useSession) {
        this.useSession = useSession;
    }
}

