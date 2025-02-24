package clap.server.application.port.outbound.taskhistory;

import clap.server.domain.model.task.Comment;
import clap.server.domain.model.task.TaskHistory;

public interface CommandTaskHistoryPort {
    TaskHistory save(TaskHistory taskHistory);

    void deleteTaskHistoryByCommentId(Long commentId);
}
