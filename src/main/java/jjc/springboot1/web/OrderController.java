package jjc.springboot1.web;

import jjc.springboot1.pojo.Order;
import jjc.springboot1.service.OrderService;
import jjc.springboot1.util.PageNavigate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    OrderService service;

    @GetMapping("/orders")
    public PageNavigate<Order> list(@RequestParam(name = "start", defaultValue = "0") int start,
                                    @RequestParam(name = "size", defaultValue = "5") int size){
        return service.list(start, size, 7);
    }

    @PutMapping("/orders/{id}")
    public Order delivery(@RequestBody Order order){
        service.delivery(order);
        return order;
    }

}
