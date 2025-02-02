package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Label;

public interface CommandLabelPort {
    void save(Label label);
}
