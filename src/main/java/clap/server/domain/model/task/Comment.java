package clap.server.domain.model.task;

import clap.server.adapter.inbound.web.dto.task.PostAndEditCommentRequest;
import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTime {
    private Long commentId;
    private Member member;
    private Task task;
    private String content;
    private boolean isModified;
    private boolean isDeleted;

    public static Comment createComment(Member member, Task task, String content) {
        return Comment.builder()
                .member(member)
                .task(task)
                .content(content)
                .isModified(false)
                .build();
    }

    public void updateComment(String content) {
        this.content = content;
        if(!this.isModified) {
            this.isModified = true;
        }
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}
