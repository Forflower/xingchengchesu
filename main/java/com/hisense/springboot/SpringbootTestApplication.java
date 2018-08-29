package com.hisense.springboot;

import com.hisense.springboot.thread.ConsumerThread;
import com.hisense.springboot.util.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@MapperScan("com.hisense.springboot.mapper")
public class SpringbootTestApplication {


	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SpringbootTestApplication.class, args);
		SpringUtil springUtil = new SpringUtil();
		springUtil.setApplicationContext(ctx);
		ConsumerThread thread = new ConsumerThread();
		thread.start();


	}
}
