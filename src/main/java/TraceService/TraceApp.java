package TraceService;

import TraceService.Business.TraceConfig;
import TraceService.Business.TraceManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAutoConfiguration
public class TraceApp {

    public static void main(String[] args) {
        SpringApplication.run(TraceApp.class, args);
    }

    @Bean
    public TraceManager traceManager() {
        return new TraceManager(traceConfig());
    }

    @Bean
    public TraceConfig traceConfig() {
        return new TraceConfig();
    }
}