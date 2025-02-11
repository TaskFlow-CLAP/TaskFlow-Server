package clap.server.adapter.outbound.persistense.repository.task;
import clap.server.adapter.outbound.persistense.entity.task.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findByIsDeletedFalse();
    List<CategoryEntity> findByIsDeletedFalseAndMainCategoryIsNull();
    List<CategoryEntity> findByIsDeletedFalseAndMainCategoryIsNotNull();

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CategoryEntity c WHERE c.mainCategory IS NULL AND c.isDeleted = false AND (c.name = :name OR c.code = :code)")
    boolean existsByNameOrCodeAndMainCategoryIsNullAndIsDeletedFalse(@Param("name") String name, @Param("code") String code);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CategoryEntity c WHERE c.mainCategory = :mainCategory AND c.isDeleted = false AND (c.name = :name OR c.code = :code)")
    boolean existsByMainCategoryAndIsDeletedFalseAndNameOrCode(@Param("mainCategory")CategoryEntity mainCategory, @Param("name") String name, @Param("code") String code);
}