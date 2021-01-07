package jjc.springboot1.web;

import jjc.springboot1.pojo.Product;
import jjc.springboot1.pojo.ProductImage;
import jjc.springboot1.service.ProductImageService;
import jjc.springboot1.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class ProductImageController {

    @Autowired
    ProductImageService service;

    /**
     * 获取产品图片
     * @param pid   产品ID
     * @return
     */
    @GetMapping("/products/{pid}/productImages")
    public List<ProductImage> list(@PathVariable("pid") int pid){
        return service.list(pid);
    }

    /**
     * 删除产品图片
     * @param id 产品图片的ID
     * @return
     */
    @DeleteMapping("/productImages/{id}")
    public Object delete(@PathVariable("id") int id){
        service.delete(id);
        return null;
    }

    /**
     * 添加产品图片
     * @param
     * @return
     */
    @PostMapping("/productImages")
    public ProductImage add(MultipartFile file,
                            @RequestParam(name="pid") String pid,
                            @RequestParam(name="type") String type,
                            HttpServletRequest request) throws IOException {
        ProductImage productImage = new ProductImage();
        productImage.setType(type);
        Product product = new Product();
        product.setId(Integer.parseInt(pid));
        productImage.setProduct(product);
        service.add(productImage);
        saveOrUpdateImage(productImage, file, request);
        return productImage;
    }


    /**
     * 将上传的文件保存在服务器中
     * @param bean  要保存的分类
     * @param image  要保持的文件
     * @param request   请求用于获取目录
     */
    public void saveOrUpdateImage(ProductImage bean, MultipartFile image, HttpServletRequest request) throws IOException {
        if(image == null){
            //不进行图片的更新
            return;
        }

        //创建要保存的目标文件对象
        File folder = new File(request.getServletContext().getRealPath("/img/product"));
        File img = new File(folder, bean.getId() + ".jpg");
        if(!img.getParentFile().exists()){
            img.getParentFile().mkdir();
        }
        image.transferTo(img);   //将上传的文件保存到本地
        BufferedImage bufferedImage = ImageUtil.change2jpg(img);    //将二进制数据转换为图片
        ImageIO.write(bufferedImage, "jpg", img);   //将转换后的图片数据,写回文件
    }

    @GetMapping("/productImages/{id}")
    public ProductImage get(@PathVariable("id") int id){
        return service.get(id);
    }
}
