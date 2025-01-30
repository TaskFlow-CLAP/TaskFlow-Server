package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.ApprovalTaskRequest;
import clap.server.adapter.inbound.web.dto.task.ApprovalTaskResponse;
import clap.server.adapter.inbound.web.dto.task.FindApprovalFormResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApprovalTaskService implements ApprovalTaskUsecase {

    private final MemberService memberService;
    private final TaskService taskService;
    private final CategoryService categoryService;
    private final LabelService labelService;
    private final CommandTaskPort commandTaskPort;

    @Override
    @Transactional
    public ApprovalTaskResponse approvalTaskByReviewer(Long reviewerId, Long taskId, ApprovalTaskRequest approvalTaskRequest) {
        Member reviewer = memberService.findReviewer(reviewerId);
        Task task = taskService.findById(taskId);
        Member processor = memberService.findById(approvalTaskRequest.processorId());
        Category category = categoryService.findById(approvalTaskRequest.categoryId());
        Label label = labelService.findById(approvalTaskRequest.labelId());

        task.approveTask(reviewer, processor, approvalTaskRequest.dueDate(), category, label);
        //담당자 할당에 대한 히스토리 생성

        return TaskMapper.toApprovalTaskResponse(commandTaskPort.save(task));
    }


    @Override
    public FindApprovalFormResponse findApprovalForm(Long managerId, Long taskId) {
        memberService.findActiveMember(managerId);
        Task task = taskService.findById(taskId);
        task.validateTaskRequested();
        return TaskMapper.toFindApprovalFormResponse(task);
    }
}
