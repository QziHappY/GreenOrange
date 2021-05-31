package com;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */

@SpringBootApplication(scanBasePackages={"com"})
@MapperScan("com.Dao")
public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello GreenOrange!" );
        SpringApplication.run(App.class,args);
    }
}
