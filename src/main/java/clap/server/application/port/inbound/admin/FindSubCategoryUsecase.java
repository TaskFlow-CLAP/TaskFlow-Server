package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.response.FindSubCategoryResponse;

import java.util.List;

public interface FindSubCategoryUsecase {
    List<FindSubCategoryResponse> findSubCategory();
    FindSubCategoryResponse findOneSubCategory(Long categoryId);
}
