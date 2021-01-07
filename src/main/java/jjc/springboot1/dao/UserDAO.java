package jjc.springboot1.dao;

import jjc.springboot1.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserDAO extends JpaRepository<User, Integer> {

    User findByName(String name);    //通过用户名查询用户
}
