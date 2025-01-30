package clap.server.application.service.comment;

import clap.server.adapter.inbound.web.dto.task.PostAndEditCommentRequest;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.application.port.inbound.comment.CommandCommentUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.outbound.task.CommandCommentPort;
import clap.server.application.port.outbound.task.LoadCommentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Comment;
import clap.server.domain.model.task.Task;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.CommentErrorCode;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class CommandCommentService implements CommandCommentUsecase {

    private final MemberService memberService;
    private final LoadCommentPort loadCommentPort;
    private final CommandCommentPort commandCommentPort;

    @Transactional
    @Override
    public void updateComment(Long userId, Long commentId, PostAndEditCommentRequest request) {

        Member member = memberService.findActiveMember(userId);

        Comment comment = loadCommentPort.findById(commentId)
                .orElseThrow(() -> new ApplicationException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (checkCommenter(comment.getTask(), member)) {

            comment.updateComment(request.content());
            commandCommentPort.saveComment(comment);
        };
    }


    public Boolean checkCommenter(Task task, Member member) {
        // 일반 회원일 경우 => 요청자인지 확인
        // 담당자일 경우 => 처리자인지 확인
        if ((member.getMemberInfo().getRole() == MemberRole.ROLE_MANAGER)
                && !(member.getMemberId() == task.getProcessor().getMemberId())) {
            throw new ApplicationException(MemberErrorCode.NOT_A_COMMENTER);
        }

        else if ((member.getMemberInfo().getRole() == MemberRole.ROLE_USER)
                && !(member.getMemberId() == task.getRequester().getMemberId())) {
            throw new ApplicationException(MemberErrorCode.NOT_A_COMMENTER);
        }
        else {
            return true;
        }
    }
}
