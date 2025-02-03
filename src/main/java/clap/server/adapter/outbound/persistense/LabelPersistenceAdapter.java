package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.label.response.FindLabelListResponse;
import clap.server.adapter.outbound.persistense.entity.task.LabelEntity;
import clap.server.adapter.outbound.persistense.mapper.LabelPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.task.LabelRepository;
import clap.server.application.mapper.LabelMapper;
import clap.server.application.port.outbound.task.CommandLabelPort;
import clap.server.application.port.outbound.task.LoadLabelPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.task.Label;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class LabelPersistenceAdapter implements LoadLabelPort, CommandLabelPort {

    private final LabelRepository labelRepository;
    private final LabelPersistenceMapper labelPersistenceMapper;

    @Override
    public Optional<Label> findById(Long labelId) {
        Optional<LabelEntity> labelEntity = labelRepository.findById(labelId);
        return labelEntity.map(labelPersistenceMapper::toDomain);
    }

    @Override
    public List<Label> findLabelList() {
        List<LabelEntity> labelEntities = labelRepository.findByIsDeletedFalse();

        return labelEntities.stream()
                .map(labelPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public SliceResponse<FindLabelListResponse> findLabelListBySlice(Pageable pageable) {
        Slice<Label> labelList = labelRepository.findByIsDeletedFalse(pageable)
                .map(labelPersistenceMapper::toDomain);

        return LabelMapper.toSliceOfFindNoticeListResponse(
                labelList.map(LabelMapper::toFindLabelListResponse)
        );
    }

    @Override
    public void save(Label label) {
        LabelEntity labelEntity = labelPersistenceMapper.toEntity(label);
        labelRepository.save(labelEntity);
    }
}
