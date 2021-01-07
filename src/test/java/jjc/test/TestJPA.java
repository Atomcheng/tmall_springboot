package jjc.test;

import jjc.springboot1.TmallSpringbootApplication1;
import jjc.springboot1.dao.CategoryDAO;
import jjc.springboot1.pojo.Category;
import jjc.springboot1.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TmallSpringbootApplication1.class)
public class TestJPA {

    @Autowired
    CategoryDAO dao;
    @Autowired
    CategoryService service;

    @Test
    public void test(){

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(0, 5, sort);
        Page<Category> page = dao.findAll(pageable);
    }
}
