package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.response.FindAllCategoryResponse;
import clap.server.adapter.inbound.web.dto.admin.response.FindSubCategoryResponse;
import clap.server.application.mapper.CategoryResponseMapper;
import clap.server.application.port.inbound.admin.FindAllCategoryUsecase;
import clap.server.application.port.outbound.task.LoadCategoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.task.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class FindAllCategoryService implements FindAllCategoryUsecase {
    private final LoadCategoryPort loadCategoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<FindAllCategoryResponse> findAllCategory() {
        List<Category> categories = loadCategoryPort.findAll();
        return categories.stream().filter(category -> category.getMainCategory() == null)
                .map(parent -> CategoryResponseMapper.toFindAllCategoryResponse(
                        parent.getCategoryId(),
                        parent.getName(),
                        parent.getCode(),
                        getSubCategories(categories, parent) // 2차 카테고리 리스트 변환
                ))
                .toList();
    }

    private List<FindSubCategoryResponse> getSubCategories(List<Category> categories, Category parent) {
        return categories.stream()
                .filter(category -> category.getMainCategory() != null &&
                                    category.getMainCategory().getCategoryId().equals(parent.getCategoryId())) // 부모가 같은 것들 필터링
                .map(CategoryResponseMapper::toFindSubCategoryResponse)
                .toList();
    }
}