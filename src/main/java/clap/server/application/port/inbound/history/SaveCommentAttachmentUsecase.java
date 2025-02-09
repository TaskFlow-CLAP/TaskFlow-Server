package clap.server.application.port.inbound.history;

import org.springframework.web.multipart.MultipartFile;

public interface SaveCommentAttachmentUsecase {

    void saveCommentAttachment(Long memberId, Long taskId, MultipartFile file);
}
