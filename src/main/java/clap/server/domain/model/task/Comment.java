package clap.server.domain.model.task;

import clap.server.adapter.inbound.web.dto.task.PostAndEditCommentRequest;
import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTime {
    private Long commentId;
    private Member member;
    private Task task;
    private String content;
    private boolean isModified;

    public static Comment createComment(Member member, Task task, PostAndEditCommentRequest request) {
        return Comment.builder()
                .member(member)
                .task(task)
                .content(request.content())
                .isModified(false)
                .build();
    }

}
