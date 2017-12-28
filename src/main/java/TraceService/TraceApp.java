package TraceService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;

@SpringBootApplication
@EnableAutoConfiguration
public class TraceApp
{
    public static void main(String[] args)
    {
        SpringApplication.run(TraceApp.class, args);
    }
}