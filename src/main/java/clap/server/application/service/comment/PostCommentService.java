package clap.server.application.service.comment;

import clap.server.adapter.inbound.web.dto.task.PostAndEditCommentRequest;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.application.mapper.AttachmentMapper;
import clap.server.application.port.inbound.comment.PostCommentUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.outbound.task.CommandCommentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.common.constants.FilePathConstants;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Comment;
import clap.server.domain.model.task.Task;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class PostCommentService implements PostCommentUsecase {

    private final MemberService memberService;
    private final TaskService taskService;
    private final CommandCommentPort commandCommentPort;

    @Transactional
    @Override
    public void save(Long userId, Long taskId, PostAndEditCommentRequest request) {
        Task task = taskService.findById(taskId);
        Member member = memberService.findActiveMember(userId);

        // 일반 회원일 경우 => 요청자인지 확인
        // 담당자일 경우 => 처리자인지 확인
        if ((member.getMemberInfo().getRole() == MemberRole.ROLE_MANAGER)
                && !(member.getMemberId() == task.getProcessor().getMemberId())) {
            throw new ApplicationException(MemberErrorCode.NOT_A_COMMENTER);
        }

        if ((member.getMemberInfo().getRole() == MemberRole.ROLE_USER)
                && !(member.getMemberId() == task.getRequester().getMemberId())) {
            throw new ApplicationException(MemberErrorCode.NOT_A_COMMENTER);
        }

        Comment comment = Comment.createComment(member, task, request);
        commandCommentPort.save(comment);
    }
}
