package hku.cs;

import hku.cs.entity.FileProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileProperties.class})
public class ProjApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjApplication.class, args);
    }

}
