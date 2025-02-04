package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Task;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface CommandTaskPort {
    Task save(Task task);

    void updateAgitPostId(ResponseEntity<String> responseEntity, Task task);
}