package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Category;

import java.util.Optional;

public interface LoadCategoryPort {
    Optional<Category> findById(Long id);
}
