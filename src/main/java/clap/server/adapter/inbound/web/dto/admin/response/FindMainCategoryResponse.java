package clap.server.adapter.inbound.web.dto.admin.response;

public record FindMainCategoryResponse(
        Long id,
        String name,
        String code
) {
}
