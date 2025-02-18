package clap.server.application.port.inbound.domain;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.port.outbound.task.CommandTaskPort;
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
    private final CommandTaskPort commandTaskPort;

    public Task findById(Long taskId) {
        return loadTaskPort.findById(taskId).orElseThrow(
                ()-> new ApplicationException(TaskErrorCode.TASK_NOT_FOUND));
    }
    
    public Task upsert(Task task) {
        return commandTaskPort.save(task);
    }

    public Task findByIdAndStatus(Long taskId, TaskStatus status) {
        return loadTaskPort.findByIdAndStatus(taskId, status).orElseThrow(() -> new ApplicationException(TaskErrorCode.TASK_NOT_FOUND));
    }

    public Task findTaskWithProcessorDepartment(Long taskId){
        return loadTaskPort.findTaskWithProcessorDepartment(taskId).orElseThrow(()-> new ApplicationException(TaskErrorCode.TASK_NOT_FOUND));
    }
}
