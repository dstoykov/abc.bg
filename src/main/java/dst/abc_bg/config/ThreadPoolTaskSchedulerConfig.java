package dst.abc_bg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ThreadPoolTaskSchedulerConfig {
    private static final String THREAD_POOL_TASK_SCHEDULER = "ThreadPoolTaskScheduler_";
    private static final Integer SCHEDULER_POOL_SIZE = 5;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler
                = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(SCHEDULER_POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix(THREAD_POOL_TASK_SCHEDULER);

        return threadPoolTaskScheduler;
    }
}