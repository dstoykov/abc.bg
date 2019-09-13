package dst.abc_bg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AbcBgApplication {
    public static void main(String[] args) {
        SpringApplication.run(AbcBgApplication.class, args);
    }
}
