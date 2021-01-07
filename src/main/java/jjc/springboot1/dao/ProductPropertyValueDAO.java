package jjc.springboot1.dao;


import jjc.springboot1.pojo.Product;
import jjc.springboot1.pojo.ProductPropertyValue;
import jjc.springboot1.pojo.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPropertyValueDAO extends JpaRepository<ProductPropertyValue, Integer> {
    List<ProductPropertyValue> findByProduct(Product product);
}
