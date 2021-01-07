package jjc.springboot1.interceptor;

import jjc.springboot1.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//在处理请求前进行登录认证拦截
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String[] requireAuthPages = {  //需要进行登录检查的请求
                "buy",
                "alipay",
                "payed",
                "cart",
                "bought",
                "confirmPay",
                "orderConfirmed",

                "forebuyone",
                "forebuy",
                "foreaddCart",
                "forecart",
                "forechangeOrderItem",
                "foredeleteOrderItem",
                "forecreateOrder",
                "forepayed",
                "forebought",
                "foreconfirmPay",
                "foreorderConfirmed",
                "foredeleteOrder",
                "forereview",
                "foredoreview"

        };
        String uri = request.getRequestURI();   //获取请求的URI
        String contextPath = request.getContextPath(); //获取上下文路径
        String page = StringUtils.remove(uri, contextPath + "/");   //将上下文移除方便用于比较
        boolean found = false;
        for (String requireAuthPage : requireAuthPages) {
            if (StringUtils.startsWith(requireAuthPage, page)) {
                found = true;

            }
        }
        if(found) {  //对需要认证的页面进行处理
            if (user == null) {
                response.setStatus(301);
                response.sendRedirect("login"); //用户未登录，跳转至登录页面
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
