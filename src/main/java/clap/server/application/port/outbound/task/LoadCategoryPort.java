package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Category;

import java.util.List;
import java.util.Optional;

public interface LoadCategoryPort {
    Optional<Category> findById(Long id);

    List<Category> findAll();
    List<Category> findMainCategory();
    List<Category> findSubCategory();

    boolean existsMainCategoryByNameOrCode(Category category, String name, String code);

    boolean existsSubCategoryByNameOrCode(Category category, Category mainCategory, String name, String code);
}
