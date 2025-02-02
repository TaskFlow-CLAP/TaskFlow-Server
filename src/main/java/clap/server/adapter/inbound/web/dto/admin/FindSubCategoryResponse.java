package clap.server.adapter.inbound.web.dto.admin;

public record FindSubCategoryResponse(
        Long id,
        Long mainCategoryId,
        String name,
        String code
) {
}
