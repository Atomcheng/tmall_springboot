package jjc.springboot1;

import jjc.springboot1.util.PortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching  //启动缓存
public class TmallSpringbootApplication1 {
    static {
        PortUtil.checkPort(6379,"Redis 服务端",true);
    }

    public static void main(String[] args) {
        SpringApplication.run(TmallSpringbootApplication1.class, args);
    }

}
