package jjc.springboot1.web;

import jjc.springboot1.pojo.Property;
import jjc.springboot1.service.PropertyService;
import jjc.springboot1.util.PageNavigate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PropertyController {

    @Autowired
    PropertyService propertyService;

    /**
     * 分页查询
     * @param cid   所属分类
     * @param start     起始页
     * @param size      每页条数
     * @return  分页信息和数据信息
     */
    @GetMapping("/categories/{cid}/properties")
    public PageNavigate<Property> list(@PathVariable("cid")int cid,
                                       @RequestParam(name="start", defaultValue = "0") int start,
                                       @RequestParam(name="size", defaultValue = "5") int size
                                       ){
        PageNavigate<Property> page = propertyService.list(cid, start, size, 7);
        return page;
    }

    /**
     * 增加属性
     * @param property 要增加的属性,根据前端提交的JSON数据自动注入对象
     * @return  属性对象
     */
    @PostMapping("/properties")
    public Object add(@RequestBody Property property){
        propertyService.add(property);
        return property;
    }

    /**
     * 删除属性
     * @param id 属性id
     * @return  null
     */
    @DeleteMapping("/properties/{id}")
    public Object delete(@PathVariable("id") int id){
        propertyService.delete(id);
        return null;
    }

    /**
     * 获取属性
     * @param id 属性id
     * @return 给定id的属性(Json字符串)
     */
    @GetMapping("/properties/{id}")
    public Property get(@PathVariable("id") int id){
        return propertyService.get(id);
    }


    @PutMapping("/properties/{id}")
    public Object update(@RequestBody Property property){
        propertyService.update(property);
        return property;
    }

}
