package clap.server.adapter.inbound.web.dto.admin;

import java.util.List;

public record FindAllCategoryResponse(
        List<FindMainCategoryResponse> mainCategory,
        List<FindSubCategoryResponse> subCategory
) {
}
