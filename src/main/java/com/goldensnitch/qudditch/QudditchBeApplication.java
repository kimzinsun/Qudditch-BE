package com.goldensnitch.qudditch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.goldensnitch.qudditch.mapper")
public class QudditchBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(QudditchBeApplication.class, args);
    }

}
