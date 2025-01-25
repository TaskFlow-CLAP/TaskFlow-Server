package clap.server.application.Task;

import clap.server.adapter.inbound.web.dto.task.ApprovalTaskRequest;
import clap.server.adapter.inbound.web.dto.task.ApprovalTaskResponse;
import clap.server.application.mapper.TaskMapper;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.LabelService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.ApprovalTaskUsecase;
import clap.server.application.port.outbound.task.CommandTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Label;
import clap.server.domain.model.task.Task;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class ApprovalTaskService implements ApprovalTaskUsecase {

    private final MemberService memberService;
    private final TaskService taskService;
    private final CategoryService categoryService;
    private final LabelService labelService;
    private final CommandTaskPort commandTaskPort;

    @Override
    @Transactional
    public ApprovalTaskResponse approvalTaskByReviewer(Long reviewerId, ApprovalTaskRequest approvalTaskRequest) {
        Member reviewer = memberService.findActiveMember(reviewerId);
        if (!reviewer.isReviewer()) {
            throw new ApplicationException(MemberErrorCode.NOT_A_REVIEWER);
        }
        Task task = taskService.findById(approvalTaskRequest.taskId());
        Member processor = memberService.findById(approvalTaskRequest.processorId());
        Category mainCategory = categoryService.findById(approvalTaskRequest.mainCategoryId());
        Category category = categoryService.findById(approvalTaskRequest.categoryId());
        Label label = labelService.findById(approvalTaskRequest.labelId());

        task.approveTask(reviewer, processor, approvalTaskRequest.dueDate(),mainCategory, category, label);
        return TaskMapper.toApprovalTaskResponse(commandTaskPort.save(task));
    }
}
