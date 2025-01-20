package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.task.CategoryEntity;
import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MemberPersistenceMapper.class})
public interface CategoryPersistenceMapper extends PersistenceMapper<CategoryEntity, Category> {


    default boolean mapIsDeleted(Category category) {
        return category.isDeleted();
    }

    default boolean mapIsDeleted(CategoryEntity categoryEntity) {
        return categoryEntity.isDeleted();
    }
}
