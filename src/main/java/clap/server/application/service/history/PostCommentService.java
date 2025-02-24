package clap.server.application.service.history;

import clap.server.adapter.inbound.web.dto.history.request.CreateCommentRequest;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.history.SaveCommentAttachmentUsecase;
import clap.server.application.port.inbound.history.SaveCommentUsecase;
import clap.server.application.port.outbound.s3.S3UploadPort;
import clap.server.application.port.outbound.task.CommandCommentPort;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.application.service.webhook.SendNotificationService;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.common.constants.FilePathConstants;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Comment;
import clap.server.domain.model.task.Task;
import clap.server.domain.model.task.TaskHistory;
import clap.server.domain.policy.task.TaskCommentPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static clap.server.domain.model.task.Attachment.formatFileSize;

@ApplicationService
@RequiredArgsConstructor
public class PostCommentService implements SaveCommentUsecase, SaveCommentAttachmentUsecase {

    private final MemberService memberService;
    private final TaskService taskService;
    private final CommandCommentPort commandCommentPort;
    private final S3UploadPort s3UploadPort;

    private final CommandTaskHistoryPort commandTaskHistoryPort;
    private final SendNotificationService sendNotificationService;
    private final TaskCommentPolicy taskCommentPolicy;

    @Transactional
    @Override
    public void save(Long memberId, Long taskId, CreateCommentRequest request) {
        Task task = taskService.findById(taskId);
        Member member = memberService.findActiveMember(memberId);

        // 일반 회원일 경우 => 요청자인지 확인
        taskCommentPolicy.validateCommentPermission(task, member);
        Comment comment = Comment.createComment(member, task, request.content(), null, null, null);
        Comment savedComment = commandCommentPort.saveComment(comment);

        TaskHistory taskHistory = TaskHistory.createCommentTaskHistory(TaskHistoryType.COMMENT, member, savedComment);
        commandTaskHistoryPort.save(taskHistory);

        Member processor = task.getProcessor();
        Member requester = task.getRequester();
        Member receiver = Objects.equals(member.getMemberId(), requester.getMemberId()) ? processor : requester;

        if (receiver != null) {
            publishNotification(receiver, task, request.content(), member.getNickname());
        }
    }

    @Transactional
    @Override
    public void saveCommentAttachment(Long memberId, Long taskId, MultipartFile file) {
        Task task = taskService.findById(taskId);
        Member member = memberService.findActiveMember(memberId);

        taskCommentPolicy.validateCommentPermission(task, member);

        String fileUrl = s3UploadPort.uploadSingleFile(FilePathConstants.TASK_COMMENT, file);
        String fileName = file.getOriginalFilename();

        Comment comment = Comment.createComment(member, task, null, fileName, fileUrl, formatFileSize(file.getSize()));
        Comment savedComment = commandCommentPort.saveComment(comment);

        TaskHistory taskHistory = TaskHistory.createCommentTaskHistory(TaskHistoryType.COMMENT_FILE, member, savedComment);
        commandTaskHistoryPort.save(taskHistory);

        Member processor = task.getProcessor();
        Member requester = task.getRequester();
        Member receiver = Objects.equals(member.getMemberId(), requester.getMemberId()) ? processor : requester;
        String senderNickname = Objects.equals(member.getMemberId(), requester.getMemberId()) ? requester.getNickname() : processor.getNickname();

        if (receiver != null) {
            publishNotification(receiver, task, fileName + "(첨부파일)", senderNickname);
        }

    }

    private void publishNotification(Member receiver, Task task, String message, String commenterName) {
        boolean isManager = receiver.getMemberInfo().getRole() == MemberRole.ROLE_MANAGER;
        sendNotificationService.sendPushNotification(receiver, NotificationType.COMMENT, task, message, null, commenterName, isManager);
    }
}
