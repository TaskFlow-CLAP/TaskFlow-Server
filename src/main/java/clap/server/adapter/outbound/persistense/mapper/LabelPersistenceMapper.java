package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.task.LabelEntity;
import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;
import clap.server.domain.model.task.Label;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LabelPersistenceMapper extends PersistenceMapper<LabelEntity, Label> {
    default boolean mapIsDeleted(Label label) {
        return label.isDeleted();
    }

    default boolean mapIsDeleted(LabelEntity entity) {
        return entity.isDeleted();
    }
}
