package clap.server.application.port.inbound.history;

import org.springframework.web.multipart.MultipartFile;

public interface SaveCommentAttachmentUsecase {

    void saveCommentAttachment(Long userId, Long taskId, MultipartFile file);
}
