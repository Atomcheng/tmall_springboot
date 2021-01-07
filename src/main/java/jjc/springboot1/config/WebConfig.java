package jjc.springboot1.config;

import jjc.springboot1.interceptor.LoginInterceptor;
import jjc.springboot1.interceptor.OtherInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 全局配置.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * 跨域请求配置，允许所有源，所有方式，所有头对服务器资源进行访问.
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");

    }

    /**
     * 配置拦截器，对所有请求进行拦截。
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(getOtherInterceptor())
                .addPathPatterns("/**"); //对所有请求进行拦截
        registry.addInterceptor(getLoginInterceptor())
                .addPathPatterns("/**"); //对所有请求进行拦截
    }

    /**
     * 将创建拦截器对象交给spring处理
     * @return
     */
    @Bean
    public LoginInterceptor getLoginInterceptor(){
        return new LoginInterceptor();
    }

    /**
     * 将创建拦截器对象交给spring处理
     * @return
     */
    @Bean
    public OtherInterceptor getOtherInterceptor(){
        return new OtherInterceptor();
    }
}
