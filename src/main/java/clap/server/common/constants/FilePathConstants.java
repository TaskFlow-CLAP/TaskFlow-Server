package clap.server.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilePathConstants {
    TASK_FILE("task"),
    TASK_COMMENT("task/comments"),
    MEMBER_IMAGE("member"),
    ;
    private final String path;

}
