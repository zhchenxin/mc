package top.chenxin.mc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.chenxin.mc.mapper")
public class McApplication {

	public static void main(String[] args) {
		SpringApplication.run(McApplication.class, args);
	}
}
