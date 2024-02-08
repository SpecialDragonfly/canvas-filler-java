package orme.dominic.canvasfiller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class CanvasFillerConfig {
    @Bean(name = "GeneratorThreads")
    public ThreadPoolTaskExecutor generatorThreadsTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Generator-");
        executor.setRejectedExecutionHandler((r, executor1) -> System.out.println("Task rejected, thread pool is full and queue is also full"));
        executor.initialize();
        return executor;
    }

    @Bean(name = "PixelServiceThreads")
    public ThreadPoolTaskExecutor pixelServiceTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("PixelService-");
        executor.setRejectedExecutionHandler((r, executor1) -> System.out.println("Task rejected, thread pool is full and queue is also full"));
        executor.initialize();
        return executor;
    }
}
