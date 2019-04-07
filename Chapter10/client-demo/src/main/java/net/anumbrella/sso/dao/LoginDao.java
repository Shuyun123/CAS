package net.anumbrella.sso.dao;


import net.anumbrella.sso.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Anumbrella
 */
@Repository
public interface LoginDao extends CrudRepository<User, Long> {

    List<User> findByUsernameAndPassword(String name, String password);
}