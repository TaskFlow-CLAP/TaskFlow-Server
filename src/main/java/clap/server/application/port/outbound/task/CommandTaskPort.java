package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Task;

public interface CommandTaskPort {
    Task save(Task task);
}