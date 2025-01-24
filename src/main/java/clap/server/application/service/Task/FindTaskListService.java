package clap.server.application.service.Task;

import clap.server.adapter.inbound.web.dto.task.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.FindTaskListResponse;

import clap.server.application.port.inbound.domain.MemberService;

import clap.server.application.port.inbound.task.FindTaskListUsecase;

import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;

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
    public Page<FindTaskListResponse> findRequestedTaskList(Long requesterId, Pageable pageable, FilterTaskListRequest findTaskListRequest) {
        Member requester = memberService.findActiveMember(requesterId);
        return loadTaskPort.findAllByRequesterId(requester.getMemberId(), pageable, findTaskListRequest);
    }
}
