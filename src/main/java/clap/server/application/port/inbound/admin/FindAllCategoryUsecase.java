package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.FindAllCategoryResponse;

public interface FindAllCategoryUsecase {
    FindAllCategoryResponse findAllCategory();
}
