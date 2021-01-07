package jjc.springboot1.web;

import jjc.springboot1.pojo.Category;
import jjc.springboot1.service.CategoryService;
import jjc.springboot1.util.ImageUtil;
import jjc.springboot1.util.PageNavigate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 处理产品分类相关的异步请求
 */
@RestController
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 查询分页显示
     * @param start 要显示的页数,基于0
     * @param size  每页条数
     * @return
     */
    @GetMapping("/categories")
    public PageNavigate<Category> list(@RequestParam(name="start", defaultValue = "0")int start,
                                       @RequestParam(name = "size", defaultValue = "5")int size){
        start = start < 0 ? 0 : start;
        PageNavigate  pageNavigate= categoryService.list(start, size, 7);
        return pageNavigate;
    }

    /**
     * 异步删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/categories/{id}")
    public Object delete(@PathVariable("id")int id){
        categoryService.delete(id);
        return null;
    }

    /**
     * 异步添加数据,数据会自动注入,在数据库增加条目后,会将生成的id注入对象
     */
    @PostMapping("/categories")
    public Object add(Category category, MultipartFile image, HttpServletRequest request) throws IOException {
        categoryService.add(category);
        saveOrUpdateImage(category, image, request);
        return category;
    }

    /**
     * 将上传的文件保存在服务器中
     * @param bean  要保存的分类
     * @param image  要保持的文件
     * @param request   请求用于获取目录
     */
    public void saveOrUpdateImage(Category bean, MultipartFile image, HttpServletRequest request) throws IOException {
        if(image == null){
            //不进行图片的更新
            return;
        }

        //创建要保存的目标文件对象
        File folder = new File(request.getServletContext().getRealPath("/img/category"));
        File img = new File(folder, bean.getId() + ".jpg");
        if(!img.getParentFile().exists()){
            img.getParentFile().mkdir();
        }
        image.transferTo(img);   //将上传的文件保存到本地
        BufferedImage bufferedImage = ImageUtil.change2jpg(img);    //将二进制数据转换为图片
        ImageIO.write(bufferedImage, "jpg", img);   //将转换后的图片数据,写回文件
    }

    /**
     * 获取指定id的分类信息,发送给前端
     */
    @GetMapping("/categories/{id}")
    public Category get(@PathVariable("id")int id){
        Category category = categoryService.get(id);
        return category;
    }

    /**
     * 更新分类信息
     */
    @PutMapping("/categories/{id}")
    public Object update(Category category, MultipartFile image, HttpServletRequest request)
            throws IOException {
        String name = request.getParameter("name");     //name无法自动注入category(PUT方式的缺陷)
        category.setName(name);
        saveOrUpdateImage(category, image, request);
        categoryService.update(category);
        return category;
    }
}
