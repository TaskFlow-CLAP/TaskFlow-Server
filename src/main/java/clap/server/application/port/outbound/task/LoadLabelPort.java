package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Label;

import java.util.Optional;

public interface LoadLabelPort {

    Optional<Label> findById(Long id);
}
