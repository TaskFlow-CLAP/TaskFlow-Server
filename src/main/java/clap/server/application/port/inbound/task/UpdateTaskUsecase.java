package clap.server.application.port.inbound.task;


import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.response.UpdateTaskResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UpdateTaskUsecase {
    UpdateTaskResponse updateTask(Long memberId, Long taskId, UpdateTaskRequest updateTaskRequest, List<MultipartFile> files);
}
