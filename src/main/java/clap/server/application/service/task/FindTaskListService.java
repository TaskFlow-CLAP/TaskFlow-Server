package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.common.PageResponse;

import clap.server.adapter.inbound.web.dto.task.request.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.response.FilterAllTasksResponse;
import clap.server.adapter.inbound.web.dto.task.response.FilterAssignedTaskListResponse;
import clap.server.adapter.inbound.web.dto.task.response.FilterPendingApprovalResponse;
import clap.server.adapter.inbound.web.dto.task.response.FilterRequestedTasksResponse;
import clap.server.application.mapper.response.TaskResponseMapper;
import clap.server.application.port.inbound.domain.MemberService;

import clap.server.application.port.inbound.task.FindTaskListUsecase;

import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;

import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindTaskListService implements FindTaskListUsecase {

    private final MemberService memberService;
    private final LoadTaskPort loadTaskPort;


    @Override
    public PageResponse<FilterRequestedTasksResponse> findTasksRequestedByUser(Long requesterId, Pageable pageable, FilterTaskListRequest findTaskListRequest) {
        Member requester = memberService.findActiveMember(requesterId);
        return PageResponse.from(loadTaskPort.findTasksRequestedByUser(requester.getMemberId(), pageable, findTaskListRequest)
                .map(TaskResponseMapper::toFilterRequestedTasksResponse));
    }

    @Override
    public PageResponse<FilterAssignedTaskListResponse> findTasksAssignedByManager(Long processorId, Pageable pageable, FilterTaskListRequest findTaskListRequest) {
        Member processor = memberService.findActiveMember(processorId);
        return PageResponse.from(loadTaskPort.findTasksAssignedByManager(processor.getMemberId(), pageable, findTaskListRequest)
                .map(TaskResponseMapper::toFilterAssignedTaskListResponse));
    }

    @Override
    public PageResponse<FilterPendingApprovalResponse> findPendingApprovalTasks(Long managerId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        memberService.findActiveMember(managerId);
        Page<Task> taskList = loadTaskPort.findPendingApprovalTasks(pageable, filterTaskListRequest);
        return PageResponse.from(taskList.map(TaskResponseMapper::toFilterPendingApprovalTasksResponse));
    }

    @Override
    public PageResponse<FilterAllTasksResponse> findAllTasks(Long managerId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        memberService.findActiveMember(managerId);
        return PageResponse.from(loadTaskPort.findAllTasks(pageable, filterTaskListRequest)
                .map(TaskResponseMapper::toFilterAllTasksResponse));
    }
}
