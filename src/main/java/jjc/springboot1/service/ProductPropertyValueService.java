package jjc.springboot1.service;

import jjc.springboot1.dao.ProductDAO;
import jjc.springboot1.dao.ProductPropertyValueDAO;
import jjc.springboot1.dao.PropertyDAO;
import jjc.springboot1.pojo.Product;
import jjc.springboot1.pojo.ProductPropertyValue;
import jjc.springboot1.pojo.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "pvs")
public class ProductPropertyValueService {

    @Autowired
    ProductPropertyValueDAO dao;
    @Autowired
    ProductDAO productDAO;
    @Autowired
    PropertyDAO propertyDAO;

    /**
     * 查询属性值
     * @param pid 产品ID
     * @return
     */
    @Cacheable(key = "'pvs-product' + #p0")
    public List<ProductPropertyValue> list(int pid){    //可以进行改造
        Product product = productDAO.findOne(pid);
        List<ProductPropertyValue> ptValues = dao.findByProduct(product);
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        ArrayList<Property> properties = propertyDAO.findByCategory(product.getCategory(), sort);
        if (ptValues.size() == 0){   //数据库中没有属性值
            for(Property property : properties){
                ProductPropertyValue ptValue= new ProductPropertyValue();
                ptValue.setProduct(product);
                ptValue.setProperty(property);
                dao.save(ptValue);
                ptValues.add(ptValue);
            }
        }

        int i = 0;
        int propertiesSize = properties.size();
        while( ptValues.size() < propertiesSize) {   //有新添加的属性,将其写入属性值表内
            ProductPropertyValue ptValue= new ProductPropertyValue();
            Property property = properties.get(propertiesSize - 1 - i );
            ptValue.setProduct(product);
            ptValue.setProperty(property);
            dao.save(ptValue);
            ptValues.add(ptValue);
            i++;
        }
        return ptValues;
    }

    /**
     * 更新产品值
     * @param bean 产品值对象
     */
    @CacheEvict(allEntries = true)
    public void update(ProductPropertyValue bean){

        dao.save(bean);
    }
}
