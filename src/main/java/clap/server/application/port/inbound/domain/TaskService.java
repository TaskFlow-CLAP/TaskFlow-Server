package clap.server.application.port.inbound.domain;

import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.domain.model.task.Task;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final LoadTaskPort loadTaskPort;

    public Task findById(Long taskId) {
        return loadTaskPort.findById(taskId).orElseThrow(
                ()-> new ApplicationException(TaskErrorCode.TASK_NOT_FOUND));
    }
}
