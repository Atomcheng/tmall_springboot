package jjc.springboot1.service;

import jjc.springboot1.dao.CategoryDAO;
import jjc.springboot1.pojo.Category;
import jjc.springboot1.pojo.Product;
import jjc.springboot1.util.PageNavigate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务类，用于处理产品分类的数据
 */
@Service
@CacheConfig(cacheNames = "categories")
public class CategoryService {
    @Autowired
    CategoryDAO categoryDAO;
    /**
     * 分页查询业务
     * @return
     */
    @Cacheable(key="'categories-page-' + #p0 + '-' + #p1")
    public PageNavigate<Category> list(int start, int size, int pageNavigateNum){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Category> page = categoryDAO.findAll(pageable);
        return new PageNavigate<Category>(page, pageNavigateNum);
    }

    /**
     * 删除分类
     */
    @CacheEvict(allEntries = true) //将redis里面的所有缓存数据清空，保证数据一致性
    public void delete(int id ){
        categoryDAO.delete(id);
    }

    /**
     * 增加分类
     * @param category 要增加的分类
     */
    @CacheEvict(allEntries = true) //将redis里面的所有缓存数据清空，保证数据一致性
    public void add(Category category){
        categoryDAO.save(category);
    }

    /**
     * 获取指定的分类
     */
    @Cacheable(key = "'categories-one' + #p0")
    public Category get(int id){
        return categoryDAO.findOne(id);
    }

    /**
     * 更新分类
     */
    @CacheEvict(allEntries = true) //将redis里面的所有缓存数据清空，保证数据一致性
    public void update(Category category){
        categoryDAO.save(category);
    }

    /**
     * 从产品中移除分类，防止转换为Json时出现递归错误
     * @param category 分类
     */
    public void removeCategoryFromProduct(Category category){
        List<Product> products = category.getProducts();
        if(products != null){
            for(Product product : products){
                product.setCategory(null);
            }
        }
        List<List<Product>> productsByRow = category.getProductsByRow();
        if(productsByRow != null){
            for(List<Product> ps : productsByRow){
                for(Product p : ps){
                    p.setCategory(null);
                }
            }
        }
    }

    /**
     * 移除产品内的分类，防止防止转换为Json时出现递归错误
     * @param categories 产品分类
     */
    public void removeCategoryFromProduct(List<Category> categories){
        for(Category category : categories){
            removeCategoryFromProduct(category);
        }
    }

    /**
     * 查询所有的分类
     * @return
     */
    public List<Category> list(){
        return categoryDAO.findAll();
    }


}
