package clap.server.adapter.outbound.persistense;

import clap.server.adapter.outbound.persistense.entity.task.CategoryEntity;
import clap.server.adapter.outbound.persistense.mapper.CategoryPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.task.CategoryRepository;
import clap.server.application.port.outbound.task.CommandCategoryPort;
import clap.server.application.port.outbound.task.LoadCategoryPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.task.Category;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements LoadCategoryPort, CommandCategoryPort {

    private final CategoryRepository categoryRepository;
    private final CategoryPersistenceMapper categoryPersistenceMapper;

    @Override
    public Optional<Category> findById(final Long id) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(id);
        return categoryEntity.map(categoryPersistenceMapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findByIsDeletedFalse()
                .stream()
                .map(categoryPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findMainCategory() {
        return categoryRepository.findByIsDeletedFalseAndMainCategoryIsNull()
                .stream()
                .map(categoryPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findSubCategory() {
        return categoryRepository.findByIsDeletedFalseAndMainCategoryIsNotNull()
                .stream()
                .map(categoryPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsMainCategoryByNameOrCode(String name, String code) {
        return categoryRepository.existsByNameOrCodeAndMainCategoryIsNullAndIsDeletedFalse(name, code);
    }

    @Override
    public boolean existsSubCategoryByNameOrCode(Category category, String name, String code) {
        CategoryEntity categoryEntity = categoryPersistenceMapper.toEntity(category);
        return categoryRepository.existsByMainCategoryAndIsDeletedFalseAndNameOrCode(categoryEntity, name, code);
    }


    @Override
    public void save(final Category category) {
        categoryRepository.save(categoryPersistenceMapper.toEntity(category));
    }
}