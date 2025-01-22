package clap.server.adapter.inbound.web.dto.task;


import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;

public record FindTaskListRequest(
        Integer term,  //조회기간
        Long categoryId,
        Long mainCategoryId,
        String title,
        String nickName, //처리자 닉네임
        TaskStatus taskStatus
) {
}
