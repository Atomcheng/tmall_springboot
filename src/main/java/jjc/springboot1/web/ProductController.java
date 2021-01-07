package jjc.springboot1.web;

import jjc.springboot1.pojo.Product;
import jjc.springboot1.service.ProductService;
import jjc.springboot1.util.PageNavigate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 控制类,向前端提供对象的Json格式字符串
 */
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    /**
     * 分页显示
     * @param cid 分类id
     * @param start 页码基于0
     * @param size 每页数量
     * @return 分页信息和每页数据
     */
    @GetMapping("/categories/{cid}/products")
    public PageNavigate<Product> list(@PathVariable("cid") int cid,
                                      @RequestParam(name = "start", defaultValue = "0") int start,
                                      @RequestParam(name = "size", defaultValue = "5") int size){
        start = start < 0 ? 0 : start;
        return productService.list(cid, start, size, 7);
    }

    /**
     * 更新产品信息
     * @param product 产品对象
     * @return 产品对象
     */
    @PutMapping("/products/{id}")
    public Product update(@RequestBody Product product){
        productService.update(product);
        return product;
    }

    /**
     * 增加产品
     * @param  product 产品对象
     * @return  产品对象
     */
    @PostMapping("/products")
    public Product add(@RequestBody Product product){
        Product productAdd = productService.add(product);
        return productAdd;
    }

    /**
     * 获取产品对象
     * @param id    产品对象
     * @return  产品对象
     */
    @GetMapping("/products/{id}")
    public Product get(@PathVariable("id") int id){
        Product product = productService.get(id);
        return product;
    }

    @DeleteMapping("/products/{id}")
    public Object delete(@PathVariable("id") int id){
        productService.delete(id);
        return null;
    }

}
