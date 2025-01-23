package clap.server.adapter.inbound.web.dto.task;


import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.task.Term;

import java.util.List;

public record FindTaskListRequest(
        Term term,
        List<Long> categoryIds,
        List<Long> mainCategoryIds,
        String title,
        String nickName, //처리자 닉네임
        TaskStatus taskStatus
) {
}
