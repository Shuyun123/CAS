package net.anumbrella.rest.server.controller;


import net.anumbrella.rest.server.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anumbrella
 */
@RestController
public class RestAttributeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestAttributeController.class);


    @PostMapping("/attributes")
    public Object getAttributes(@RequestHeader HttpHeaders httpHeaders) {

        SysUser user = new SysUser();

        user.setEmail("rest@gmail.com");
        user.setUsername("email");
        user.setPassword("123");
        List<String> role = new ArrayList<>();
        role.add("admin");
        role.add("dev");
        user.setRole(role);
        //成功返回json
        return user;
    }

}

