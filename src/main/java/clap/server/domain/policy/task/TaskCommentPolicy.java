package clap.server.domain.policy.task;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.common.annotation.architecture.Policy;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;
import clap.server.exception.DomainException;
import clap.server.exception.code.MemberErrorCode;

import java.util.Objects;

@Policy
public class TaskCommentPolicy {
    public void validateCommentPermission(final Task task,final Member member) {
        boolean isUser = member.getMemberInfo().getRole() == MemberRole.ROLE_USER;
        boolean isNotRequester = !Objects.equals(member.getMemberId(), task.getRequester().getMemberId());

        if (isUser && isNotRequester) {
            throw new DomainException(MemberErrorCode.COMMENT_PERMISSION_DENIED);
        }
    }
}
