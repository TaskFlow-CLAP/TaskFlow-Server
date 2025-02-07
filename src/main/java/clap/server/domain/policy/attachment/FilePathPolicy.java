package clap.server.domain.policy.attachment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilePathPolicy {
    TASK_FILE("task"),
    TASK_COMMENT("task/comments"),
    MEMBER_IMAGE("member"),
    ;
    private final String path;

}
