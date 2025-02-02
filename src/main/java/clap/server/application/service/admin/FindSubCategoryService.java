package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.FindSubCategoryResponse;
import clap.server.application.mapper.response.CategoryResponseMapper;
import clap.server.application.port.inbound.admin.FindSubCategoryUsecase;
import clap.server.application.port.outbound.task.LoadCategoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class FindSubCategoryService implements FindSubCategoryUsecase {
    private final LoadCategoryPort loadCategoryPort;

    @Override
    @Transactional
    public List<FindSubCategoryResponse> findSubCategory() {
        return loadCategoryPort.findSubCategory()
                .stream()
                .map(CategoryResponseMapper::toFindSubCategoryResponse)
                .toList();
    }
}
