package clap.server.application.mapper.response;

import clap.server.adapter.inbound.web.dto.admin.FindAllCategoryResponse;
import clap.server.adapter.inbound.web.dto.admin.FindMainCategoryResponse;
import clap.server.adapter.inbound.web.dto.admin.FindSubCategoryResponse;
import clap.server.domain.model.task.Category;

import java.util.List;

public class CategoryResponseMapper {

    public static FindAllCategoryResponse toFindAllCategoryResponse(
            Long id,
            String name,
            String code,
            List<FindSubCategoryResponse> subCategoryResponses) {
        return new FindAllCategoryResponse(id, name, code, subCategoryResponses);
    }

    public static FindMainCategoryResponse toFindMainCategoryResponse(Category category) {
        return new FindMainCategoryResponse(category.getCategoryId(), category.getName(), category.getCode());
    }

    public static FindSubCategoryResponse toFindSubCategoryResponse(Category category) {
        return new FindSubCategoryResponse(category.getCategoryId(), category.getMainCategory().getCategoryId(), category.getName(), category.getCode());
    }
}
