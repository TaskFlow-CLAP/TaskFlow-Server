package clap.server.application.port.inbound.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;

public interface CreateMemberLogsUsecase {

    void createMemberLog(HttpServletRequest request, HttpServletResponse response, Object result,
                         LocalDateTime responseAt, LogStatus logType, String customCode, String body, Long userId);
}
