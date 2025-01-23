package clap.server.adapter.inbound.web.dto.task;


import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;


public record FilterTaskListRequest(

        Integer term,
        @NotNull
        List<Long> categoryIds,
        @NotNull
        List<Long> mainCategoryIds,
        @NotNull
        String title,
        @NotNull
        String nickName,
        @NotNull
        List<TaskStatus> taskStatus,
        @NotNull
        OrderRequest orderRequest
) {
}
