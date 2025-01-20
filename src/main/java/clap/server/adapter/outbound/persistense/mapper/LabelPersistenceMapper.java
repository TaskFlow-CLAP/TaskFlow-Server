package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.task.LabelEntity;
import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;
import clap.server.domain.model.task.Label;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MemberPersistenceMapper.class})
public interface LabelPersistenceMapper extends PersistenceMapper<LabelEntity, Label> {
    default boolean isDeleted(Label label) {
        return label.isDeleted();
    }

    default boolean isDeleted(LabelEntity entity) {
        return entity.isDeleted();
    }
}
