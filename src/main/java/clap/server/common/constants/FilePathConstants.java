package clap.server.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilePathConstants {
    TASK_IMAGE("task/image"),
    TASK_DOCUMENT("task/docs"),
    MEMBER_IMAGE("member/image"),
    ;
    private final String path;

}
