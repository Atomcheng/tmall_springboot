package jjc.springboot1.service;

import jjc.springboot1.dao.OrderItemDAO;
import jjc.springboot1.pojo.Order;
import jjc.springboot1.pojo.OrderItem;
import jjc.springboot1.pojo.Product;
import jjc.springboot1.pojo.User;
import jjc.springboot1.util.SpringContextUtil;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "orderItems")
public class OrderItemService {

    @Autowired
    OrderItemDAO dao;

    /**
     * 计算产品的销售量
     * @param product 产品
     * @return
     */
    @Cacheable(key = "'orderItems-product-' + #p0.id")
    public int getSaleCount(Product product){
        int saleCount = 0;
        List<OrderItem> orderItems = dao.findByProduct(product);
        for (OrderItem o : orderItems){
            saleCount += o.getNumber();
        }
        return saleCount;
    }

    /**
     * 查询用户下面没有创建订单的订单项
     * @param user 用户
     * @return
     */
    @Cacheable(key = "'orderItems-user-' + #p0.id")
    public List<OrderItem> listByUser(User user){
        return dao.findByUserAndOrderIsNull(user);
    }

    /**
     * 更新订单项目
     * @param item 订单项目
     */
    @CacheEvict(allEntries = true)
    public void update(OrderItem item){
        dao.save(item);
    }

    /**
     * 新增订单项
     * @param item
     */
    @CacheEvict(allEntries = true)
    public void add(OrderItem item){
        dao.save(item);
    }

    /**
     * 查询指定ID的订单项目
     * @param id 订单项ID
     * @return
     */
    @Cacheable(key = "'orderItems-' + #p0")
    public OrderItem get(int id) {
        return dao.findOne(id);
    }

    /**
     * 删除订单项
     * @param id
     */
    @CacheEvict(allEntries = true)
    public void delete(int id){
        dao.delete(id);
    }

    /**
     * 对订单对象填充订单项
     * @param order
     */
    public void fill(Order order){
        List<OrderItem> items = dao.findByOrder(order);
        order.setOrderItems(items);
    }

    /**
     * 对订单填充订单项
     * @param orders
     */
    public void fill(List<Order> orders){
        for(Order order : orders){
            fill(order);
        }
    }

    /**
     * 检查订单项是否已经存在于某个订单内
     * @param items 订单项集合
     * @return 返回false代表不存在，返回true代表存在
     */
    public boolean checkRepeatSubmitOrderItems(List<OrderItem> items){
        boolean orderExist = false;
        for (OrderItem item : items){
            OrderItem dbitem = get(item.getId());
            if(dbitem.getOrder() != null){
                orderExist = true;
                break;
            }
        }
        return orderExist;
    }


}
