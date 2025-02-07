package clap.server.adapter.inbound.web.dto.admin.response;

public record FindSubCategoryResponse(
        Long id,
        Long mainCategoryId,
        String name,
        String code,
        String descriptionExample
) {
}
