package com.dfs.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Dedicated thread pool for asynchronous request/response audit persistence, kept OFF the request path.
 *
 * Financial-grade choice: {@link ThreadPoolExecutor.CallerRunsPolicy} — under overload the calling
 * (request) thread runs the task itself, so an audit row is NEVER silently dropped. In-flight tasks
 * are drained on shutdown so nothing is lost on redeploy/restart.
 *
 * Pool is kept small so it cannot starve the DB connection pool of request-serving connections.
 */
@Configuration
public class AsyncConfig {

    @Bean("auditExecutor")
    public ThreadPoolTaskExecutor auditExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(5000);
        executor.setThreadNamePrefix("audit-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }
}
