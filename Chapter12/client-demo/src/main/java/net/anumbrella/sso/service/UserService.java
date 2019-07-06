package net.anumbrella.sso.service;


import net.anumbrella.sso.dao.UserDao;
import net.anumbrella.sso.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Anumbrella
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public List<User> list() {
        return (List<User>) userDao.findAll();
    }

    public User findUserById(Long id){
        return userDao.findOne(id);
    }

    public List<User> findByUsernameAndPassword(String username, String password){
        return userDao.findByUsernameAndPassword(username, password);
    }

    public User findByUsername(String username){
        return userDao.findByUsername(username);
    }



}