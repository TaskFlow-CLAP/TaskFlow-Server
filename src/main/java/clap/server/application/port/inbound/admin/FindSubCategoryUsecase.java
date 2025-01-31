package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.FindSubCategoryResponse;

import java.util.List;

public interface FindSubCategoryUsecase {
    List<FindSubCategoryResponse> findSubCategory();
}
