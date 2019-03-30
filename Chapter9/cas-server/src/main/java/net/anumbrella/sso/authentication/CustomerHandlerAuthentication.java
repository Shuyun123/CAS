package net.anumbrella.sso.authentication;

import net.anumbrella.sso.entity.CustomCredential;
import net.anumbrella.sso.entity.User;
import net.anumbrella.sso.exection.CheckCodeErrorException;
import org.apereo.cas.authentication.*;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author anumbrella
 */
public class CustomerHandlerAuthentication extends AbstractPreAndPostProcessingAuthenticationHandler {

    public CustomerHandlerAuthentication(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    public boolean supports(Credential credential) {
        //判断传递过来的Credential 是否是自己能处理的类型
        return credential instanceof CustomCredential;
    }

    @Override
    protected AuthenticationHandlerExecutionResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {

        CustomCredential customCredential = (CustomCredential) credential;

        String username = customCredential.getUsername();
        String password = customCredential.getPassword();
        String email = customCredential.getEmail();
        String telephone = customCredential.getTelephone();
        String capcha = customCredential.getCapcha();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String right = attributes.getRequest().getSession().getAttribute("captcha_code").toString();


        if (!capcha.equalsIgnoreCase(right)) {
            throw new CheckCodeErrorException();
        }

        // 添加邮箱和电话的判断逻辑


        System.out.println("username : " + username);
        System.out.println("password : " + password);
        System.out.println("email : " + email);
        System.out.println("telephone : " + telephone);
        System.out.println("capcha : " + capcha);
        System.out.println("right : " + right);


        // JDBC模板依赖于连接池来获得数据的连接，所以必须先要构造连接池
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/cas");
        dataSource.setUsername("root");
        dataSource.setPassword("123");

        // 创建JDBC模板
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);

        String sql = "SELECT * FROM user WHERE username = ?";

        User info = (User) jdbcTemplate.queryForObject(sql, new Object[]{username}, new BeanPropertyRowMapper(User.class));

        System.out.println("database username : " + info.getUsername());
        System.out.println("database password : " + info.getPassword());


        if (info == null) {
            throw new AccountException("Sorry, username not found!");
        }

        if (!info.getPassword().equals(password)) {
            throw new FailedLoginException("Sorry, password not correct!");
        } else {

            final List<MessageDescriptor> list = new ArrayList<>();
            // 可自定义返回给客户端的多个属性信息
            HashMap<String, Object> returnInfo = new HashMap<>();
            returnInfo.put("expired", info.getDisabled());
            returnInfo.put("email", info.getEmail());
            returnInfo.put("username", info.getUsername());
            returnInfo.put("password", info.getPassword());
            returnInfo.put("disabled", info.getDisabled());

            return createHandlerResult(customCredential,
                    this.principalFactory.createPrincipal(username, returnInfo), list);
        }


    }
}
