package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Category;

public interface CommandCategoryPort {
    void save(Category category);
}