package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.FindAllCategoryResponse;
import clap.server.adapter.inbound.web.dto.admin.FindMainCategoryResponse;
import clap.server.adapter.inbound.web.dto.admin.FindSubCategoryResponse;
import clap.server.application.mapper.response.CategoryResponseMapper;
import clap.server.application.port.inbound.admin.FindAllCategoryUsecase;
import clap.server.application.port.outbound.task.LoadCategoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class FindAllCategoryService implements FindAllCategoryUsecase {
    private final LoadCategoryPort loadCategoryPort;

    @Override
    @Transactional(readOnly = true)
    public FindAllCategoryResponse findAllCategory() {
        List<FindMainCategoryResponse> mainCategoryResponses = new ArrayList<>();
        List<FindSubCategoryResponse> subCategoryResponses = new ArrayList<>();
        loadCategoryPort.findAll().forEach(category -> {
            if (category.getMainCategory() == null) {
                mainCategoryResponses.add(CategoryResponseMapper.toFindMainCategoryResponse(category));
            } else {
                subCategoryResponses.add(CategoryResponseMapper.toFindSubCategoryResponse(category));
            }
        });
        return CategoryResponseMapper.toFindAllCategoryResponse(mainCategoryResponses, subCategoryResponses);
    }
}