package clap.server.adapter.inbound.web.dto.task.response;

public record FindApprovalFormResponse(
        Long categoryId,
        String categoryName,
        String mainCategoryName
) {
}

