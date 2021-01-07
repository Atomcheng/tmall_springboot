package jjc.springboot1.service;

import jjc.springboot1.dao.ReviewDAO;
import jjc.springboot1.pojo.Product;
import jjc.springboot1.pojo.Review;
import jjc.springboot1.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "reviews")
public class ReviewService {

    @Autowired
    ReviewDAO dao;
    @Autowired
    UserService userService;

    /**
     * 查询产品下的分类
     * @param product 产品
     * @return
     */
    @Cacheable(key = "'reviews-product-' + #p0.id")
    public List<Review> list(Product product){
        List<Review> reviews = dao.findByProductOrderById(product);
        for (Review review : reviews){
            User user = review.getUser();
            userService.setUserAnonymousName(user);
        }
        return reviews;
    }

    /**
     * 获取该产品下的评论条数
     * @param product
     * @return
     */
    @Cacheable(key = "'reviewsCount-product-' + #p0.id")
    public int getCount(Product product){
        return dao.countByProduct(product);
    }

    @CacheEvict(allEntries = true)
    public void add(Review review){
        dao.save(review);
    }
}
