package jjc.springboot1.dao;

import jjc.springboot1.pojo.Order;
import jjc.springboot1.pojo.OrderItem;
import jjc.springboot1.pojo.Product;
import jjc.springboot1.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderItemDAO extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByProduct(Product product);   //查询产品下的所有订单项目
    List<OrderItem> findByUserAndOrderIsNull(User user); //查询用户未生产订单的订单项
    List<OrderItem> findByOrder(Order order);
}
