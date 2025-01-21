package clap.server.adapter.inbound.web.dto.task;


public record FindTaskListRequest(
        Integer term,  //조회기간
        Long categoryId,
        Long mainCategoryId,
        String title,
        String nickName, //처리자 닉네임
        Long statusId //1,2,3,4,5 중 하나
) {
}
