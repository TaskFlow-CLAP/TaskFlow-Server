package clap.server.application.port.inbound.comment;

import org.springframework.web.multipart.MultipartFile;

public interface SaveCommentAttachmentUsecase {

    void saveCommentAttachment(Long userId, Long taskId, MultipartFile file);
}
