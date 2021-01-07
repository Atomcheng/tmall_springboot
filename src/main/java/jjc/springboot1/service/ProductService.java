package jjc.springboot1.service;

import jjc.springboot1.dao.ProductDAO;
import jjc.springboot1.pojo.Category;
import jjc.springboot1.pojo.Product;
import jjc.springboot1.util.PageNavigate;
import jjc.springboot1.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 业务类,进行业务逻辑处理
 */
@Service
@CacheConfig(cacheNames = "products")
public class ProductService {

    @Autowired
    ProductDAO productDAO;

    @Autowired
    ProductImageService productImageService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    OrderItemService orderItemService;
    /**
     * 按照类别进行分页查询产品
     * @param cid   分类的id
     * @param start  页码,基于0
     * @param size  每页条数
     * @param pageNums  显示的页码数
     * @return  分页和数据信息
     */
    @Cacheable(key = "'products-cid'+ #p0 + '-page-' + #p1 + '-' + #p2 ")
    public PageNavigate<Product> list(int cid, int start, int size, int pageNums){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Category category = new Category();
        category.setId(cid);
        Page<Product> page = productDAO.findByCategory(pageable, category);
        return new PageNavigate<Product>(page, pageNums);
    }

    /**
     * 删除指定的产品
     * @param id    产品id
     */
    @CacheEvict(allEntries = true)
    public void delete(int id){
        productDAO.delete(id);
    }

    /**
     * 增加产品
     * @param  product 要增加的产品
     */
    @CacheEvict(allEntries = true)
    public Product add(Product product){
        product.setCreateDate(new Date());
        productDAO.save(product);
        return product;
    }

    /**
     * 更新产品
     * @param product 要更新的产品
     */
    @CacheEvict(allEntries = true)
    public void update(Product product){
        productDAO.save(product);
    }

    /**
     * 获取产品
     * @param id 产品id
     * @return
     */
    @Cacheable(key = "'products-' + #p0")
    public Product get(int id){
       return productDAO.findOne(id);

    }

    /**
     * 向分类填充产品信息
     * @param category 要填充的分类
     */
    public void fill(Category category){
        ProductService productService = SpringContextUtil.getBean(ProductService.class);    //取出SPRING容器中的Bean,用于触发AOP
        List<Product> ps = productService.listByCategory(category);
        productImageService.setFirstProductImage(ps);
        category.setProducts(ps);
    }

    /**
     * 向一个分类集合中的分类填充产品信息。
     * @param categories 分类集合
     */
    public void fill(List<Category> categories){
        for (Category category : categories){
            fill(category);
        }
    }

    /**
     * 对分类填充推荐的产品内容
     * @param categories 分类集合
     */
    public void fillByRow(List<Category> categories){
        int productNumberEachRow = 8;   //每一行的产品数量
        for(Category category : categories){
            List<Product> products = productDAO.findByCategoryOrderById(category);
            List<List<Product>> productsByRow = new ArrayList<>();
            for (int i = 0; i < products.size(); i += productNumberEachRow){
                int size = i + productNumberEachRow;
                size = size > products.size() ? products.size() : size;
                List<Product> productOfEachRow = products.subList(i, size);
                productsByRow.add(productOfEachRow);
            }
            category.setProductsByRow(productsByRow);
        }
    }

    /**
     * 查询同一分类下的产品
     * @param category 分类
     * @return
     */
    @Cacheable(key = "'products-cid' + #p0.id")
    public List<Product> listByCategory(Category category){

        return productDAO.findByCategoryOrderById(category);
    }

    /**
     * 设置产品的销售量和产品的评价数量
     * @param product
     */
    public void setReviewAndSaleCount(Product product){
        int review = reviewService.getCount(product);
        int saleCount = orderItemService.getSaleCount(product);
        product.setReviewCount(review);
        product.setSaleCount(saleCount);
    }

    /**
     * 设置产品的销售量和产品的评价数量
     * @param products 产品集合
     */
    public void setReviewAndSaleCount(List<Product> products){
        for (Product product : products){
            setReviewAndSaleCount(product);
        }
    }

    /**
     * 对产品名字进行模糊查询
     * @param keyword 关键字
     * @param start 页码
     * @param size 每页条数
     * @return
     */
    public List<Product> search(String keyword, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        List<Product> products =productDAO.findByNameLike("%"+keyword+"%",pageable);
        return products;
    }

}
