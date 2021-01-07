package jjc.springboot1.web;

import jjc.springboot1.comparator.*;
import jjc.springboot1.pojo.*;
import jjc.springboot1.service.*;
import jjc.springboot1.util.Result;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 提供异步访问请求的处理
 */
@RestController
public class ForeRESTController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    ProductPropertyValueService pvService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ProductImageService imageService;

    @Autowired
    OrderItemService itemService;

    @Autowired
    OrderService orderService;

    /**
     * 返回首页所需数据
     * @return  返回分类和产品数据
     */
    @GetMapping("/forehome")
    public Object home(){
        List<Category> cs = categoryService.list(); //获取所有的分类信息
        productService.fill(cs); //对所有的分类填充产品信息
        productService.fillByRow(cs); //填充推荐产品信息
        categoryService.removeCategoryFromProduct(cs); //移除产品内的分类信息，防止转为JSON字符时出现无穷递归
        return cs;
    }

    /**
     * 注册页面异步数据交互
     * @param user 用户对象
     * @return
     */
    @PostMapping("/foreregister")
    public Result register(@RequestBody User user){
        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);  //对特殊符号进行转义，防止恶意注册，如<script>使用HTML标签
        if(userService.isExist(name)){   //用户名存在处理
           return Result.fail("用户名已经被使用，请使用其他用户名");
        }
        //对用户密码进行加密
        String salt = new SecureRandomNumberGenerator().nextBytes().toString(); //获取一个随机字符串，用于加密
        String passwordBeforeEncode = user.getPassword();
        String passwordAfterEncode = new SimpleHash("md5", passwordBeforeEncode,
                                                    salt, 2).toString();
        user.setPassword(passwordAfterEncode);
        user.setName(name);
        user.setSalt(salt);
        userService.add(user);
        return Result.success();    //返回注册成功
    }

    /**
     * 前台登录
     * @param user 用户对象
     * @return
     */
    @PostMapping("/forelogin")
    public Result login(@RequestBody User user, HttpSession session) {
        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);
        User dbUser = userService.getByName(name); //查询数据库内的用户
//        if (dbUser == null || !dbUser.getPassword().equals(user.getPassword())) { //用户名不存在或密码错误
//            return Result.fail("用户名或密码错误");
//        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, user.getPassword());
        try {
            subject.login(token);
            session.setAttribute("user", dbUser); //登录后增加用户信息
        }catch (AuthenticationException e){
            String message = "账号或密码错误";
            return Result.fail(message);
        }

        return Result.success();
    }

    /**
     * 产品页面数据交互
     * @param pid 产品ID
     * @return 返回map,product代表产品，pvs代表属性值集合，reviews代表评价集合
     */
    @GetMapping("/foreProduct/{pid}")
    public Object product(@PathVariable("pid") int pid){
        Product p = productService.get(pid);

        List<ProductPropertyValue> pvs = pvService.list(pid);   //获取产品的属性
        List<Review> reviews = reviewService.list(p);   //获取产品评价
        List<ProductImage> productSingleImages = imageService.listSingleImage(p);
        List<ProductImage> productDetailImages = imageService.listDetailImage(p);
        imageService.setFirstProductImage(p);   //设置产品首张图片
        p.setProductSingleImages(productSingleImages);  //设置产品单个图片
        p.setProductDetailImages(productDetailImages);  //设置产品详情图片


        int saleCount = itemService.getSaleCount(p);
        int reviewCount = reviewService.getCount(p);

        p.setSaleCount(saleCount);  //设置产品销售量
        p.setReviewCount(reviewCount);  //设置产品评价数量

        //将数据装入映射表一起返回前端
        Map<String, Object> map = new HashMap<>();
        map.put("product", p);
        map.put("pvs", pvs);
        map.put("reviews", reviews);
        return Result.success(map);
    }

    /**
     * 检查用户登录状态
     * @param session
     * @return
     */
    @GetMapping("/forecheckLogin")
    public Object checkLogin(HttpSession session){
        User user = (User) session.getAttribute("user");
        System.out.println(user);
        if(user == null){
            return Result.fail("未登录");
        }
        return Result.success();
    }

    /**
     * 分类异步数据交互
     * @param cid 分类ID
     * @param sort  分类依据
     * @return
     */
    @GetMapping("/forecategory/{cid}")
    public Object category(@PathVariable int cid,String sort) {
        Category c = categoryService.get(cid);
        productService.fill(c);
        productService.setReviewAndSaleCount(c.getProducts());
        categoryService.removeCategoryFromProduct(c);

        if(null!=sort){
            switch(sort){
                case "review":
                    Collections.sort(c.getProducts(),new ProductReviewComparator());
                    break;
                case "date" :
                    Collections.sort(c.getProducts(),new ProductDateComparator());
                    break;

                case "saleCount" :
                    Collections.sort(c.getProducts(),new ProductSaleCountComparator());
                    break;

                case "price":
                    Collections.sort(c.getProducts(),new ProductPriceComparator());
                    break;

                case "all":
                    Collections.sort(c.getProducts(),new ProductAllComparator());
                    break;
            }
        }

        return c;
    }

    /**
     * 搜索查询
     * @param keyword
     * @return
     */
    @PostMapping("/foresearch")
    public Object search( String keyword){
        if(null==keyword)
            keyword = "";
        List<Product> ps= productService.search(keyword,0,20);
        imageService.setFirstProductImage(ps);
        productService.setReviewAndSaleCount(ps);
        return ps;
    }

    @GetMapping("/forebuyone")
    public Object buyone(int pid, int num, HttpSession session){
        return buyoneAndAddCart(pid, num, session);
    }

    /**
     * 立即购买和添加到购物车，更新或添加订单项
     * @param pid 产品
     * @param num 数量
     * @param session 会话
     * @return 更新或添加的订单项目的ID
     */
    public int buyoneAndAddCart(int pid, int num, HttpSession session){
        int ooid = 0;
        User user = (User) session.getAttribute("user");
        System.out.println(user.getName() + user.getId());
        List<OrderItem> items = itemService.listByUser(user);   //查询出该用户没有生成订单的订单项目
        boolean found = false;  //标志变量，决定后面代码是否执行
        Product product = productService.get(pid);


        //查看有没有相同的产品存在,有则在存在的订单上添加数量
        for(OrderItem item : items){
            if(item.getProduct().getId() == pid){
                num = item.getNumber() + num;   //将数据库内的产品数量和新添加的进行相加合并
                item.setNumber(num);
                itemService.update(item);
                found = true; //将标志量设置为true下面添加新订单项的代码不再进行
                ooid = item.getId();
            }
        }
        if(!found){
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setUser(user);
            item.setNumber(num);
            itemService.add(item);
            ooid = item.getId();
        }
        return ooid;
    }

    /**
     * 订单结算页面数据交互,方法中将订单项放入session便于后面创建订单使用
     * @param oiid 要结算的订单结合
     * @param session 用于传递数据
     * @return
     */
    @GetMapping("/forebuy")
    public Object buy(String[] oiid, HttpSession session){
        List<OrderItem> items = new ArrayList<>();
        float total = 0; //总金额
        for(String strId : oiid){ //遍历所有订单项，进行金额计算
            int id = Integer.parseInt(strId);
            OrderItem item = itemService.get(id);
            total += item.getNumber()*item.getProduct().getPromotePrice();
            items.add(item);    //将订单项放入集合
        }
        imageService.setFirstProductImageOnOrderItems(items);   //为产品设置第一张图片，用于结算时显示
        session.setAttribute("ois", items); //将订单项集合放入session内

        Map<String, Object> map = new HashMap<>();
        map.put("orderItems", items);
        map.put("total", total);
        return Result.success(map);
    }

    /**
     * 商品增加到购物车
     * @param pid 商品ID
     * @param num 商品数量
     * @param session 会话，包含用户信息
     * @return
     */
    @GetMapping("/foreaddCart")
    public Object addCart(int pid, int num, HttpSession session){
        int oiid = buyoneAndAddCart(pid, num, session);
        return Result.success(oiid);    //返回添加成功的订单项ID

    }

    /**
     * 购物车页面数据交互
     * @param session
     * @return
     */
    @GetMapping("/forecart")
    public Object cart(HttpSession session){
        User user = (User) session.getAttribute("user");
        List<OrderItem> items = itemService.listByUser(user);   //查询出所有没有创建订单的订单项
        imageService.setFirstProductImageOnOrderItems(items);
        return items;
    }

    /**
     * 购物车页面删除订单项
     * @param oiid 订单项ID
     * @return
     */
    @GetMapping("/foredeleteOrderItem")
    public Object deleteOrderItem(int oiid){
        itemService.delete(oiid);
        return Result.success();
    }

    /**
     * 生成订单
     * @param order 订单
     * @param session
     * @return
     */
    @PostMapping("/forecreateOrder")
    public Object createOrder(@RequestBody Order order, HttpSession session){
        User user = (User)  session.getAttribute("user");
        if(null==user)
            return Result.fail("未登录");
        Date now = new Date();
        String orderCode = new SimpleDateFormat("yyMMddHHmmssSSS").format(now)
                + RandomUtils.nextInt(10000) + "";//生成订单号
        order.setOrderCode(orderCode);
        order.setCreateDate(now);
        order.setUser(user);
        order.setStatus(OrderService.waitPay);
        List<OrderItem> ois = (List<OrderItem>)session.getAttribute("ois");
        double total = orderService.add(order, ois);    //将订单添加到数据库
        Map<String, Object> map = new HashMap<>();
        map.put("oid", order.getId());
        map.put("total", total);
        return Result.success(map);
    }

    /**
     * 支付界面数据交互，设置订单状态
     * @param oid
     * @param session
     * @return
     */
    @GetMapping("/forepayed")
    public Object payed(int oid, HttpSession session){
        Order order = orderService.get(oid);
        User user = (User) session.getAttribute("user");
        if (order.getUser().getId() != user.getId()) //防止不同用户恶意篡改数据。
            return Result.fail("请登录正确用户");
        order.setStatus(OrderService.waitDelivery); //设置订单状态未待发货
        Date now = new Date();
        order.setPayDate(now);   //设置支付日期
        orderService.update(order);
        return order;
    }

    /**
     * 获取订单页所需数据
     * @param session
     * @return
     */
    @GetMapping("/forebought")
    public Object bought(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null){
            return Result.fail("未登录");
        }
        List<Order> orders = orderService.listByUserWithoutDelete(user);    //所有没有标记删除的订单
        for(Order order : orders){  //设置第一张产品图片
            imageService.setFirstProductImageOnOrderItems(order.getOrderItems());
        }
        return orders;
    }

    /**
     * 确认支付界面数据交互
     * @param oid
     * @return
     */
    @GetMapping("foreconfirmPay")
    public Object confirmPay(int oid) {
        Order order = orderService.get(oid);
        imageService.setFirstProductImageOnOrderItems(order.getOrderItems());
        return order;
    }

    /**
     * 确认收货，更新数据库订单状态和收货日期
     * @param oid
     * @return
     */
    @GetMapping("foreorderConfirmed")
    public Object orderConfirmed( int oid) {
        Order o = orderService.get(oid);
        o.setStatus(OrderService.waitReview);
        o.setConfirmDate(new Date());
        orderService.update(o);
        return Result.success();
    }

    /**
     * 删除订单
     * @param oid
     * @return
     */
    @GetMapping("foredeleteOrder")
    public Object deleteOrder(int oid){
        System.out.println("==============");
        Order o = orderService.get(oid);
        o.setStatus(OrderService.delete);
        orderService.update(o);
        return Result.success();
    }

    /**
     * 评价界面数据交互
     * @param oid
     * @return
     */
    @GetMapping("forereview")
    public Object review(int oid) {
        Order o = orderService.get(oid);
        Product p = o.getOrderItems().get(0).getProduct();
        imageService.setFirstProductImage(p);
        List<Review> reviews = reviewService.list(p);
        productService.setReviewAndSaleCount(p);
        Map<String,Object> map = new HashMap<>();
        map.put("p", p);
        map.put("o", o);
        map.put("reviews", reviews);

        return Result.success(map);
    }

    /**
     * 将用户对产品的评价保存至数据库
     * @param session 回话，获取用户信息
     * @param oid 订单ID
     * @param pid 产品ID
     * @param content 评价内容
     * @return
     */
    @PostMapping("foredoreview")
    public Object doreview( HttpSession session,int oid,int pid,String content) {
        Order o = orderService.get(oid);
        o.setStatus(OrderService.finish);
        orderService.update(o);

        Product p = productService.get(pid);
        content = HtmlUtils.htmlEscape(content);

        User user =(User)  session.getAttribute("user");
        Review review = new Review();
        review.setContent(content);
        review.setProduct(p);
        review.setCreateDate(new Date());
        review.setUser(user);
        reviewService.add(review);
        return Result.success();
    }
}
