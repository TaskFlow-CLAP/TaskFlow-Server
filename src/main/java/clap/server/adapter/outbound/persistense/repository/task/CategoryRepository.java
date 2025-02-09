package clap.server.adapter.outbound.persistense.repository.task;
import clap.server.adapter.outbound.persistense.entity.task.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findByIsDeletedFalse();
    List<CategoryEntity> findByIsDeletedFalseAndMainCategoryIsNull();
    List<CategoryEntity> findByIsDeletedFalseAndMainCategoryIsNotNull();

    boolean existsByNameOrCode(String name, String code);
}