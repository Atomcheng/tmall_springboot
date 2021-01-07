package jjc.springboot1.service;

import jjc.springboot1.dao.OrderDAO;
import jjc.springboot1.pojo.Order;
import jjc.springboot1.pojo.OrderItem;
import jjc.springboot1.pojo.User;
import jjc.springboot1.util.PageNavigate;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Null;
import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = "orders")
public class OrderService {
    public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitConfirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete = "delete";
    @Autowired
    OrderDAO dao;
    @Autowired
    OrderItemService orderItemService;

    /**
     * 订单管理分页查询
     *
     * @param start 页码
     * @param size  每页条数
     * @param pages 分页数
     * @return 分页信息和数据
     */
    @Cacheable(key = "'orders-page-' + #p0 + #p1")
    public PageNavigate<Order> list(int start, int size, int pages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Order> page = dao.findAll(pageable);
        PageNavigate<Order> orders = new PageNavigate<Order>(page, pages);
        setTotalAndNumber(orders.getContent());

        return orders;
    }

    /**
     * 对传入的订单集合进行商品总数和订单总金额设置,进行状态转换
     *
     * @param orders 订单集合
     */
    private void setTotalAndNumber(List<Order> orders) {
        for (Order order : orders) {
            double total = caculateTotal(order);
            int number = caculateNumber(order);
            order.setTotal(total);
            order.setNumber(number);
            order.setStatusDes();
        }


    }

    /**
     * 计算订单下订单金额数
     *
     * @param order 订单
     * @return 订单金额数
     */
    private double caculateTotal(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        double total = 0;
        for (OrderItem item : orderItems) {
            int number = item.getNumber();
            double price = item.getProduct().getPromotePrice();
            total += number * price;
        }
        return total;
    }

    /**
     * 计算订单上的商品数目
     *
     * @param order 订单
     * @return 订单上商品数目
     */
    private int caculateNumber(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        int number = 0;
        for (OrderItem item : orderItems) {
            number += item.getNumber();
        }
        return number;
    }

    /**
     * 发货
     *
     * @param order 要发货的订单
     */
    @CacheEvict(allEntries = true)
    public void delivery(Order order) {
        order.setStatus(waitConfirm);
        order.setDeliveryDate(new Date());
        dao.save(order);
    }

    /**
     * 增加订单，并对订单项的oid进行设置
     *
     * @param order
     * @param ois
     * @return
     */
    @CacheEvict(allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
    public double add(Order order, List<OrderItem> ois) {
        double total = 0;
        if (!orderItemService.checkRepeatSubmitOrderItems(ois)) {
            add(order);
            for (OrderItem item : ois) {
                item.setOrder(order);
                orderItemService.update(item);
                total += item.getProduct().getPromotePrice() * item.getNumber();
            }
        }
        return total;
    }

    /**
     * 增加订单
     *
     * @param order
     */
    @CacheEvict(allEntries = true)
    public void add(Order order) {
        dao.save(order);
    }

    /**
     * 获取某个订单
     */
    @Cacheable(key = "'orders-' + #p0" )
    public Order get(int id) {
        Order order = dao.findOne(id);
        orderItemService.fill(order);
        order.setTotal(caculateTotal(order));
        order.setNumber(caculateNumber(order));
        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(null);
        }
        return order;
    }

    /**
     * 更新订单
     */
    @CacheEvict(allEntries = true)
    public void update(Order order) {
        dao.save(order);
    }

    /**
     * 查询用户下的所有订单
     *
     * @param user 用户
     * @return
     */
    @Cacheable(key = "'orders-user-' + #p0.id")
    public List<Order> listByUserWithoutDelete(User user) {
        List<Order> orders = dao.findByUserAndStatusNotOrderByIdDesc(user, OrderService.delete);    //查询除了被删除的订单
        for (Order order : orders) {  //将订单项里的订单设置为空，防止无穷递归
            orderItemService.fill(order);
            order.setTotal(caculateTotal(order));
            order.setNumber(caculateNumber(order));
            for (OrderItem item : order.getOrderItems()) {
                item.setOrder(null);
            }
        }
        return orders;
    }

}

