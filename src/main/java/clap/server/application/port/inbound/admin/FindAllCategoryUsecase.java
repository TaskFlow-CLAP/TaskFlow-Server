package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.response.FindAllCategoryResponse;

import java.util.List;

public interface FindAllCategoryUsecase {
    List<FindAllCategoryResponse> findAllCategory();
}
