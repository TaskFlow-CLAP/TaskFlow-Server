package clap.server.adapter.outbound.persistense.mapper;


import clap.server.adapter.outbound.persistense.entity.task.CategoryEntity;

import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;

import clap.server.domain.model.task.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MemberPersistenceMapper.class})
public interface CategoryPersistenceMapper extends PersistenceMapper<CategoryEntity, Category> {


    @Override
    @Mapping(source = "deleted", target = "isDeleted")
    Category toDomain(final CategoryEntity entity);

    @Override
    @Mapping(source = "deleted", target = "isDeleted")
    CategoryEntity toEntity(final Category domain);
}
