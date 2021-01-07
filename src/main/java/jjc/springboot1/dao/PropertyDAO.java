package jjc.springboot1.dao;

import jjc.springboot1.pojo.Category;
import jjc.springboot1.pojo.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

/**
 * 继承JPA数据库操作接口,提供数据库操作
 */
public interface PropertyDAO extends JpaRepository<Property, Integer> {

    Page<Property> findByCategory(Category category, Pageable pageable);    //用于分页显示属性
    ArrayList<Property> findByCategory(Category category, Sort sort);   //用于属性值显示时的属性查询.
}
