package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.response.FindSubCategoryResponse;
import clap.server.application.mapper.response.CategoryResponseMapper;
import clap.server.application.port.inbound.admin.FindSubCategoryUsecase;
import clap.server.application.port.outbound.task.LoadCategoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class FindSubCategoryService implements FindSubCategoryUsecase {
    private final LoadCategoryPort loadCategoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<FindSubCategoryResponse> findSubCategory() {
        return loadCategoryPort.findSubCategory()
                .stream()
                .map(CategoryResponseMapper::toFindSubCategoryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FindSubCategoryResponse findOneSubCategory(Long categoryId) {
        return CategoryResponseMapper.toFindSubCategoryResponse(loadCategoryPort
                .findById(categoryId)
                .orElseThrow(() -> new ApplicationException(TaskErrorCode.CATEGORY_NOT_FOUND)));
    }
}
