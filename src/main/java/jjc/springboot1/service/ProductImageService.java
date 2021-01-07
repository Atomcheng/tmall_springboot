package jjc.springboot1.service;

import jjc.springboot1.dao.ProductImageDAO;
import jjc.springboot1.pojo.OrderItem;
import jjc.springboot1.pojo.Product;
import jjc.springboot1.pojo.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@CacheConfig(cacheNames = "productImages")
public class ProductImageService {

    @Autowired
    ProductImageDAO dao;

    /**
     * 查询产品图片
     * @param pid   产品ID
     * @return 产品图片的集合,包含单个和详情图片
     */
    @Cacheable(key = "'productImages-product'+ #p0")
    public List<ProductImage> list(int pid){
        Product p = new Product();
        p.setId(pid);
        return dao.findByProduct(p);
    }

    /**
     * 删除图片
     * @param id 产品图片的id
     */
    @CacheEvict(allEntries = true)
    public void delete(int id){
        dao.delete(id);
    }

    /**
     * 增加产品图片
     * @param productImage
     */
    @CacheEvict(allEntries = true)
    public void add(ProductImage productImage){
        dao.save(productImage);
    }

    /**
     * 获取图片
     */
    @Cacheable(key = "'productImages-'+ #p0")
    public ProductImage get(int id){
        return dao.findOne(id);
    }

    /**
     * 设置第一张产品图片，用于展示
     * @param product 要设置的产品
     */
    public void setFirstProductImage(Product product){
        List<ProductImage> images = dao.findByProduct(product);
        if (images != null){
            ProductImage image = images.get(0);
            image.setProduct(null);
            product.setFirstProductImage(image);
        }else{
            product.setFirstProductImage(new ProductImage());
        }
    }

    /**
     * 重载方法，对一个集合中的产品设置第一张图片
     * @param products 产品集合
     */
    public void setFirstProductImage(List<Product> products){
        for(Product product : products){
            setFirstProductImage(product);
        }
    }

    /**
     * 返回产品的单个图片集合
     * @param product 产品对象
     * @return  返回产品的单个图片集合
     */
    @Cacheable(key = "'productImages-singel-product-' + #p0.id")
    public List<ProductImage> listSingleImage(Product product){
        List<ProductImage> productImages = dao.findByProductAndType(product, "single");
        removeProductFromProductImage(productImages);
        return productImages;
    }

    /**
     * 返回产品的详情图片集合
     * @param product 产品对象
     * @return  返回产品的详情图片集合
     */
    @Cacheable(key = "'productImages-detail-product-' + #p0.id")
    public List<ProductImage> listDetailImage(Product product){
        List<ProductImage> productImages = dao.findByProductAndType(product, "detail");
        removeProductFromProductImage(productImages);
        return productImages;
    }

    /**
     * 移除产品图片内的产品信息，防止无穷递归
     * @param image 图片

     */
    public void removeProductFromProductImage(ProductImage image){
        image.setProduct(null);
    }

    /**
     * 移除产品图片内的产品信息，防止无穷递归
     * @param images 图片集合
     */
    public void removeProductFromProductImage(List<ProductImage> images){
        for (ProductImage image : images){
            removeProductFromProductImage(image);
        }
    }

    public void setFirstProductImageOnOrderItem(OrderItem orderItem){
        Product product = orderItem.getProduct();
        setFirstProductImage(product);
    }

    public void setFirstProductImageOnOrderItems(Collection<OrderItem> orderItems){
        for(OrderItem item : orderItems){
            setFirstProductImageOnOrderItem(item);
        }
    }


}
