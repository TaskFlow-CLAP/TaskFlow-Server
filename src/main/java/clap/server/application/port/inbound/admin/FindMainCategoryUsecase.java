package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.response.FindMainCategoryResponse;

import java.util.List;

public interface FindMainCategoryUsecase {
    List<FindMainCategoryResponse> findMainCategory();
}
