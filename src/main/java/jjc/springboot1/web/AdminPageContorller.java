package jjc.springboot1.web;

import jjc.springboot1.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * 用于页面跳转
 */
@Controller
public class AdminPageContorller {

    @Autowired
    CategoryService categoryService;

    /**
     * 将路径重定向到/admin_category_list
     * @return
     */
    @GetMapping("/admin")
    public String redirectCategoryList(){

        return "redirect:/admin_category_list";
    }

    /**
     * 服务端跳转到分类展示页面
     */
    @GetMapping("/admin_category_list")
    public String listCategory(){
        return "admin/listCategory";
    }

    /**
     * 跳转至分类编辑页面
     *
     */
    @GetMapping("/admin_category_edit")
    public String editCategory(){
        return "admin/editCategory";
    }

    /**
     * 跳转至分类属性显示页面
     */
    @GetMapping("/admin_property_list")
    public String listProperty(){
        return "admin/listProperty";
    }

    /**
     * 跳转到属性修改页
     */
    @GetMapping("/admin_property_edit")
    public String editProperty(){
        return "admin/editProperty";
    }

    /**
     * 跳转至产品列表
     * @return
     */
    @GetMapping("/admin_product_list")
    public String listProduct(){
        return "admin/listProduct";
    }

    /**
     * 跳转至产品编辑
     * @return
     */
    @GetMapping("/admin_product_edit")
    public String editProduct(){
        return "admin/editProduct";
    }

    /**
     * 跳转至产品图片列表
     * @return
     */
    @GetMapping("/admin_productImage_list")
    public String listProductImage(){
        return "admin/listProductImage";
    }

    /**
     * 跳转至产品属性值编辑
     * @return
     */
    @GetMapping("/admin_product_editPropertyValue")
    public String editPropertyValue(){
        return "admin/editProductPropertyValue";
    }

    /**
     *跳转至用户列表
     * @return
     */
    @GetMapping("/admin_user_list")
    public String listUser(){
        return "admin/listUser";
    }

    /**
     * 跳转至订单管理界面
     * @return
     */
    @GetMapping("/admin_order_list")
    public String listOrder(){
        return "admin/listOrder";
    }
}
