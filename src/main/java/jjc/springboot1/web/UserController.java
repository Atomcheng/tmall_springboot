package jjc.springboot1.web;

import jjc.springboot1.pojo.User;
import jjc.springboot1.service.UserService;
import jjc.springboot1.util.PageNavigate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService service;

    /**
     * 分页查询
     * @param start 页码
     * @param size 每页显示条数
     * @return 分页信息和每页数据
     */
    @GetMapping("/users")
    public PageNavigate<User> list(@RequestParam(name = "start", defaultValue = "0") int start,
                                   @RequestParam(name = "size", defaultValue = "5") int size){
        return service.list(start, size, 7);
    }
}
