package clap.server.domain.model.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilePath {
    TASK_IMAGE("task/image"),
    TASK_DOCUMENT("task/docs"),
    MEMBER_IMAGE("member/image"),
    ;
    private final String path;

}
