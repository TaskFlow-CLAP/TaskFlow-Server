package clap.server.application.port.inbound.domain;

import clap.server.application.port.outbound.task.LoadCommentPort;
import clap.server.domain.model.task.Comment;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.CommentErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final LoadCommentPort loadCommentPort;

    public Comment findById(Long commentId) {
        return loadCommentPort.findById(commentId)
                .orElseThrow(() -> new ApplicationException(CommentErrorCode.COMMENT_NOT_FOUND));
    }
}
