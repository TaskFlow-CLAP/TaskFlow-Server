package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.response.FindMainCategoryResponse;
import clap.server.application.mapper.response.CategoryResponseMapper;
import clap.server.application.port.inbound.admin.FindMainCategoryUsecase;
import clap.server.application.port.outbound.task.LoadCategoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class FindMainCategoryService implements FindMainCategoryUsecase {
    private final LoadCategoryPort loadCategoryPort;

    @Override
    @Transactional
    public List<FindMainCategoryResponse> findMainCategory() {
        return loadCategoryPort.findMainCategory()
                .stream()
                .map(CategoryResponseMapper::toFindMainCategoryResponse)
                .toList();
    }
}
