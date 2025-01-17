package clap.server.domain.model.task;

import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment extends BaseTime {
    private Long attachmentId;
    private Member task;
    private Comment comment;
    private String fileName;
    private String fileUrl;
    private String fileSize;
}
