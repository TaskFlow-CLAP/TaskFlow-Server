package clap.server.adapter.outbound.persistense;

import clap.server.adapter.outbound.persistense.entity.task.AttachmentEntity;
import clap.server.adapter.outbound.persistense.mapper.AttachmentPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.task.AttachmentRepository;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.LoadAttachmentPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.task.Attachment;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@PersistenceAdapter
@RequiredArgsConstructor
public class AttachmentPersistenceAdapter implements CommandAttachmentPort, LoadAttachmentPort {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentPersistenceMapper attachmentPersistenceMapper;


    @Override
    public void save(final Attachment attachment) {
        attachmentRepository.save(attachmentPersistenceMapper.toEntity(attachment));
    }


    @Override
    public void saveAll(final List<Attachment> attachments) {
        List<AttachmentEntity> attachmentEntities = attachments.stream()
                .map(attachmentPersistenceMapper::toEntity)
                .collect(Collectors.toList());
        attachmentRepository.saveAll(attachmentEntities);
    }

    @Override
    public List<Attachment> findAllByTaskId(final Long taskId) {
        List<AttachmentEntity> attachmentEntities = attachmentRepository.findAllByTask_TaskId(taskId);
        return attachmentEntities.stream()
                .map(attachmentPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Attachment> findAllByTaskIdAndAttachmentId(final Long taskId, final List<Long> attachmentIds) {
        List<AttachmentEntity> attachmentEntities = attachmentRepository.findAllByTask_TaskIdAndAttachmentIdIn(taskId, attachmentIds);
        return attachmentEntities.stream()
                .map(attachmentPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
