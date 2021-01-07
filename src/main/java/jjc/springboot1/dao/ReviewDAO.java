package jjc.springboot1.dao;

import jjc.springboot1.pojo.Product;
import jjc.springboot1.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewDAO extends JpaRepository<Review, Integer> {

    List<Review> findByProductOrderById(Product product);
    int countByProduct(Product product);
}
