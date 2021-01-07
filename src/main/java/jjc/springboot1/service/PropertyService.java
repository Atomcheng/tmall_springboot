package jjc.springboot1.service;

import jjc.springboot1.dao.CategoryDAO;
import jjc.springboot1.dao.PropertyDAO;
import jjc.springboot1.pojo.Category;
import jjc.springboot1.pojo.Property;
import jjc.springboot1.util.PageNavigate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "properties")
public class PropertyService {

    @Autowired
    PropertyDAO propertyDAO;

    /**
     * 分页查询业务
     */
    @Cacheable(key = "'properties-page-cid-' + #p0 + '-' + #p1 + '-' + #p2")
    public PageNavigate<Property> list(int cid, int start, int size, int pageNavigatorNum){
        Category category = new Category();
        category.setId(cid);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Property> page = propertyDAO.findByCategory(category, pageable);
        return new PageNavigate<Property>(page, pageNavigatorNum);
    }

    /**
     * 增加属性
     */
    @CacheEvict(allEntries = true)
    public void add(Property property){
        propertyDAO.save(property);
    }

    /**
     * 删除属性
     */
    @CacheEvict(allEntries = true)
    public void delete(int id){
        propertyDAO.delete(id);
    }

    /**
     * 获取给定id的属性
     * @param id 属性id
     * @return
     */
    @Cacheable(key = "'properties-' + #p0")
    public Property get(int id){
        return propertyDAO.findOne(id);
    }

    @CacheEvict(allEntries = true)
    public void update(Property property){
        propertyDAO.save(property);
    }
}
