package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.response.TaskBoardResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface GetTaskBoardUsecase {
    TaskBoardResponse getTaskBoards(Long processorId, LocalDate untilDate,Pageable pageable);
}