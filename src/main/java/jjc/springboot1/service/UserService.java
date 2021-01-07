package jjc.springboot1.service;

import jjc.springboot1.dao.UserDAO;
import jjc.springboot1.pojo.User;
import jjc.springboot1.util.PageNavigate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "users")
public class UserService {

    @Autowired
    UserDAO dao;

    /**
     * 分页查询,显示用户名
     * @param start
     * @param size
     * @param pages
     * @return
     */
    @Cacheable(key = "'users-page-' + #p0 + '-' + #p1")
    public PageNavigate<User> list(int start , int size, int pages){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<User> page = dao.findAll(pageable);    //传递过去的用户名密码应该是加密过后的字符串
        return new PageNavigate<>(page, 7);
    }

    /**
     * 通过用户名查询用户
     * @param name  用户名
     * @return  返回用户对象
     */
    @Cacheable(key="'users-one-name-'+ #p0")
    public User getByName(String name){
        return dao.findByName(name);
    }

    /**
     * 判断用户名是否存在
     * @param name 用户名
     * @return true代表存在，false代表不存在
     */
    public boolean isExist(String name){
        User user = getByName(name);
        return user != null;
    }

    /**
     * 增加用户
     * @param user 用户对象
     */
    @CacheEvict(allEntries = true)
    public void add(User user){
        dao.save(user);
    }

    public void setUserAnonymousName(User user){
        String name = user.getName();
        if(name.length() == 1 || name.length() == 2){
            user.setAnonymousName("**");
        }else {
            String starts = "";
            int start = (int)Math.random()*10;
            String first = name.substring(0, 1);
            String last = name.substring(name.length() - 1);
            for(int i = 0; i <= start; i++){
                starts += "*";
            }
            user.setAnonymousName(first + starts + last);
        }
    }
}
