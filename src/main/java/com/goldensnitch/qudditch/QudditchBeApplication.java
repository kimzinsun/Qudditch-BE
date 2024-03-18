package com.goldensnitch.qudditch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan({"com.goldensnitch.qudditch.mapper", "com.goldensnitch.qudditch.repository"})
@ComponentScan(basePackages = {"com.goldensnitch.qudditch"}) // 추가된 부분
public class QudditchBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(QudditchBeApplication.class, args);
    }

}
