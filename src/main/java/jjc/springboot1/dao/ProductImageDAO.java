package jjc.springboot1.dao;

import jjc.springboot1.pojo.Product;
import jjc.springboot1.pojo.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageDAO extends JpaRepository<ProductImage, Integer> {

    List<ProductImage> findByProduct(Product product);
    List<ProductImage> findByProductAndType(Product product, String type);
}
