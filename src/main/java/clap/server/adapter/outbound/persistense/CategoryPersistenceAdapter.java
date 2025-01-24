package clap.server.adapter.outbound.persistense;

import clap.server.adapter.outbound.persistense.entity.task.CategoryEntity;
import clap.server.adapter.outbound.persistense.mapper.CategoryPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.task.CategoryRepository;
import clap.server.application.port.outbound.task.CommandCategoryPort;
import clap.server.application.port.outbound.task.LoadCategoryPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.task.Category;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements LoadCategoryPort, CommandCategoryPort {

    private final CategoryRepository categoryRepository;
    private final CategoryPersistenceMapper categoryPersistenceMapper;

    @Override
    public Optional<Category> findById(Long id) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(id);
        return categoryEntity.map(categoryPersistenceMapper::toDomain);
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(categoryPersistenceMapper.toEntity(category));
    }
}