package jjc.springboot1.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * 该类用于前台页面的跳转
 */
@Controller
public class ForePageController {

    /**
     * 客户端重定向到/home路径
     * @return
     */
    @GetMapping(value="/")
    public String index(){
        return "redirect:home";
    }

    /**
     * 服务器跳转至home页面，访问home网页资源
     * @return
     */
    @GetMapping(value="/home")
    public String home(){
        return "fore/home";
    }

    /**
     * 跳转至用户注册页面
     * @return
     */
    @GetMapping("/register")
    public String register(){
        return "fore/register";
    }

    /**
     * 跳转至注册成功页面
     * @return
     */
    @GetMapping("/registerSuccess")
    public String registerSuccess(){
        return "fore/registerSuccess";
    }

    /**
     * 跳转至用户登录页面
     * @return
     */
    @GetMapping("/login")
    public String login(){
        return "fore/login";
    }

    /**
     * 登录退出，跳转至首页。
     * @param session
     * @return
     */
    @GetMapping("/forelogout")
    public String logout(HttpSession session){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated())
            subject.logout();
        //session.removeAttribute("user");  //使用shiro进行提出
        return "redirect:home";
    }

    /**
     * 跳转到产品页
     * @return
     */
    @GetMapping("/product")
    public String product(){
        return "fore/product";
    }

    /**
     * 跳转到分类页
     * @return
     */
    @GetMapping("/category")
    public String category(){
        return "fore/category";
    }

    /**
     * 跳转至搜索页面
     * @return
     */
    @GetMapping("/search")
    public String searchResult(){
        return "fore/search";
    }

    /**
     * 跳转到订单结算页面
     * @return
     */
    @GetMapping("/buy")
    public String buy(){
        return "fore/buy";
    }

    /**
     * 跳转至购物车页面
     * @return
     */
    @GetMapping("cart")
    public String cart(){
        return "fore/cart";
    }

    /**
     * 跳转至支付页面
     */
    @GetMapping("/alipay")
    public String alipay(){
        return "fore/alipay";
    }

    /**
     * 跳转至支付成功页面
     */
    @GetMapping("/payed")
    public String payed(){
        return "fore/payed";
    }

    /**
     * 跳转至订单页面
     */
    @GetMapping("/bought")
    public String bought(){
        return "fore/bought";
    }

    /**
     * 跳转至确认支付页面
     */
    @GetMapping("/confirmPay")
    public String confirmPay(){
        return "fore/confirmPay";
    }

    /**
     * 跳转至收货完成界面
     */
    @GetMapping("/orderConfirmed")
    public String orderConfirmed(){
        return "fore/orderConfirmed";
    }

    /**
     * 跳转至评价界面
     */
    @GetMapping("/review")
    public String review(){
        return "fore/review";
    }
}
