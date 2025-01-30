package clap.server.application.port.inbound.comment;

import clap.server.adapter.inbound.web.dto.task.PostAndEditCommentRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostCommentUsecase {

    void save(Long userId, Long taskId, PostAndEditCommentRequest request);

    void saveCommentAttachment(Long userId, Long taskId, List<MultipartFile> files);
}
