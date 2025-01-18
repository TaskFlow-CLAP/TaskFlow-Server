package clap.server.application.port.outbound.task;

import org.springframework.data.redis.stream.Task;

import java.util.Optional;

public interface LoadTaskPort {
    Optional<Task> findById(Long id);
}