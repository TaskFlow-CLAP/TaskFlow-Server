package clap.server.adapter.inbound.web.dto.admin;

import java.util.List;

public record FindAllCategoryResponse(
        Long id,
        String name,
        String code,
        List<FindSubCategoryResponse> subCategory
) {
}
