package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTaskBoardRequest;
import clap.server.adapter.inbound.web.dto.task.response.TaskBoardResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface FilterTaskBoardUsecase {
    TaskBoardResponse getTaskBoardByFilter(Long processorId, LocalDate fromDate, FilterTaskBoardRequest request);
}
