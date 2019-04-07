package net.anumbrella.sso.service;


import net.anumbrella.sso.dao.LoginDao;
import net.anumbrella.sso.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Anumbrella
 */
@Service
public class LoginService {

    @Autowired
    private LoginDao loginDao;

    public boolean verifyLogin(User user) {

        List<User> userList = loginDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        return userList.size() > 0;
    }

}