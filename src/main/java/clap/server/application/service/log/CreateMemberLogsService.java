package clap.server.application.service.log;

import clap.server.adapter.outbound.persistense.ApiLogPersistenceAdapter;
import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.log.CreateMemberLogsUsecase;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.log.MemberLog;
import clap.server.domain.model.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@ApplicationService
@RequiredArgsConstructor
public class CreateMemberLogsService implements CreateMemberLogsUsecase {

    private final ApiLogPersistenceAdapter apiLogPersistenceAdapter;
    private final MemberService memberService;

    @Override
    @Transactional
    public void createMemberLog(HttpServletRequest request, HttpServletResponse response, Object result,
                                LocalDateTime responseAt, LogStatus logType, String customCode, String body, Long userId) {
        Member member = memberService.findById(userId);
        MemberLog memberLog = MemberLog.createMemberLog(request, response, result, responseAt, logType, customCode, body, member);
        apiLogPersistenceAdapter.saveMemberLog(memberLog);
    }
}
