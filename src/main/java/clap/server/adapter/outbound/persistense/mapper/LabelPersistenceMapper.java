package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.task.LabelEntity;
import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;
import clap.server.domain.model.task.Label;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MemberPersistenceMapper.class})
public interface LabelPersistenceMapper extends PersistenceMapper<LabelEntity, Label> {
    @Override
    @Mapping(source = "deleted", target = "isDeleted") // entity -> domain에서 isDeleted를 매핑
    Label toDomain(final LabelEntity entity);

    @Override
    @Mapping(source = "deleted", target = "isDeleted") // domain -> entity에서 isDeleted를 매핑
    LabelEntity toEntity(final Label domain);
}
