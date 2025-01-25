package clap.server.adapter.outbound.persistense.repository;

import clap.server.adapter.outbound.persistense.entity.task.LabelEntity;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.mapper.LabelPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.task.LabelRepository;
import clap.server.application.port.outbound.task.LoadLabelPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.task.Label;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class LabelPersistenceAdapter implements LoadLabelPort {

    private final LabelRepository labelRepository;
    private final LabelPersistenceMapper labelPersistenceMapper;

    @Override
    public Optional<Label> findById(Long id) {
        Optional<LabelEntity> labelEntity = labelRepository.findById(id);
        return labelEntity.map(labelPersistenceMapper::toDomain);
    }
}
