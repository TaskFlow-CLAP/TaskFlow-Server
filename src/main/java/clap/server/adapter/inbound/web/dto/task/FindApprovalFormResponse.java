package clap.server.adapter.inbound.web.dto.task;

public record FindApprovalFormResponse(
        Long categoryId,
        String categoryName,
        String mainCategoryName
) {
}

