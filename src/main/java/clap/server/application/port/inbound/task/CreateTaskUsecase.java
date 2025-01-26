package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.CreateTaskResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CreateTaskUsecase {
    CreateTaskResponse createTask(Long requesterId, CreateTaskRequest createTaskRequest, List<MultipartFile> files);
}
