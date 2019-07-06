package net.anumbrella.sso.controller;

import net.anumbrella.sso.config.WebSecurityConfig;
import net.anumbrella.sso.entity.ResponseResult;
import net.anumbrella.sso.entity.User;
import net.anumbrella.sso.service.UserService;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author Anumbrella
 */
// @Controller
@RestController
@RequestMapping("/api/user")
public class IndexController {


    @Autowired
    private HttpServletRequest request;


    @Autowired
    private HttpServletResponse response;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<User> index() {
        return userService.list();
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseResult login(@RequestBody User user) {
        if (user.getUsername() != null && user.getPassword() != null) {
            List<User> list = userService.findByUsernameAndPassword(user.getUsername(), user.getPassword());
            if (list.size() > 0 && list.get(0) != null) {
                if (user.getUsername().equals(list.get(0).getUsername()) && user.getPassword().equals(list.get(0).getPassword())) {
                    request.getSession().setAttribute(WebSecurityConfig.SESSION_LOGIN, list.get(0));
                    return new ResponseResult("登录成功", 200);
                }
            }
            return new ResponseResult("登录失败，用户名或密码不对", 500);
        }
        return new ResponseResult("登录失败，用户名或密码为空", 500);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public User info() {
        HttpSession session = request.getSession();
        if (session != null) {
            User value = (User) session.getAttribute(WebSecurityConfig.SESSION_LOGIN);
            if (value != null) {
                return userService.findUserById(value.getId());
            }
        }
        return null;
    }

    @RequestMapping(value = "/caslogin", method = RequestMethod.GET)
    public void caslogin() throws IOException {
        HttpSession session = request.getSession();
        Assertion assertion = (Assertion) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        if (assertion != null) {
            //获取登录用户名
            String username = assertion.getPrincipal().getName();
            System.out.println("user ---------> " + username);
            User temp = userService.findByUsername(username);
            System.out.println("TEMP user ---------> " + (temp.getUsername()));
            if (temp != null) {
                session.setAttribute(WebSecurityConfig.SESSION_LOGIN, temp);

                String jsessionid = session.getId();

                System.out.println("jsessionid ------> " + jsessionid);

                // 使用url传递参数,跳转到前端
                // response.sendRedirect("http://front.anumbrella.net:8000/home?jsessionid=" + jsessionid);

                // 使用nginx代理,跳转到前端
                response.sendRedirect("http://nginx.anumbrella.net:81/home");
            }
        }
    }

    @RequestMapping(value = "/caslogout", method = RequestMethod.GET)
    public void caslogout() throws IOException {
        request.getSession().removeAttribute(WebSecurityConfig.SESSION_LOGIN);
        // 退出后跳转到前端
        response.sendRedirect("http://front.anumbrella.net:8000");
    }


    @RequestMapping(value = "/logout/{id}", method = RequestMethod.GET)
    public ResponseResult logout(@PathVariable("id") Long id) {
        User temp = userService.findUserById(id);
        if (temp != null) {
            request.getSession().removeAttribute(WebSecurityConfig.SESSION_LOGIN);
            return new ResponseResult("注销成功", 200);
        }
        return new ResponseResult("注销失败", 500);
    }

}
