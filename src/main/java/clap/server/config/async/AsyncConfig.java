package clap.server.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "notificationExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 스레드 풀 크기 설정 (최대 1000명 사용자를 위한 설정 예시)
        executor.setCorePoolSize(50);  // 기본적으로 유지할 스레드 수
        executor.setMaxPoolSize(200);  // 최대 스레드 수
        executor.setQueueCapacity(1000);  // 큐의 크기 (백로그)
        executor.setThreadNamePrefix("async-task-");  // 스레드 이름 접두사

        // 스레드가 유휴 상태로 일정 시간이 지나면 종료되도록 설정
        executor.setKeepAliveSeconds(60);  // 60초 후 유휴 스레드 종료

        executor.initialize();
        return executor;
    }

    @Bean(name = "emailExecutor")
    public ThreadPoolTaskExecutor emailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 스레드 풀 크기 설정 (100명 사용자를 위한 설정)
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-email-");

        executor.setKeepAliveSeconds(60);

        executor.initialize();
        return executor;
    }

}
