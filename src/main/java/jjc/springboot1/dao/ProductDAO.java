package jjc.springboot1.dao;

import jjc.springboot1.pojo.Category;
import jjc.springboot1.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * dao类,条件查询需按规范自定义方法
 */
public interface ProductDAO extends JpaRepository<Product, Integer> {
    Page<Product> findByCategory(Pageable pageable, Category category);
    List<Product> findByCategoryOrderById(Category category);
    List<Product> findByNameLike(String keyword, Pageable pageable);    //根据名字进行模糊查询
}
