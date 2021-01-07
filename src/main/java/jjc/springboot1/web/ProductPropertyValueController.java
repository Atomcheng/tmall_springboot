package jjc.springboot1.web;

import jjc.springboot1.pojo.ProductPropertyValue;
import jjc.springboot1.service.ProductPropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductPropertyValueController {
    @Autowired
    ProductPropertyValueService service;

    /**
     * 查询业务
     * @param pid 产品ID
     * @return 属性值列表
     */
    @GetMapping("/products/{pid}/productPropertyValues")
    public List<ProductPropertyValue> list(@PathVariable("pid") int pid){
        return service.list(pid);
    }

    /**
     *更新属性值
     * @param bean 属性值对象
     * @return  更新后属性值对象
     */
    @PutMapping("/productPropertyValues/{id}")
    public ProductPropertyValue update(@RequestBody ProductPropertyValue bean){
        System.out.println(bean.getValue());
        service.update(bean);
        return bean;
    }
}
