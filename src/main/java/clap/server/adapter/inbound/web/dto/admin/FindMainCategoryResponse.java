package clap.server.adapter.inbound.web.dto.admin;

public record FindMainCategoryResponse(
        Long id,
        String name,
        String code
) {
}
