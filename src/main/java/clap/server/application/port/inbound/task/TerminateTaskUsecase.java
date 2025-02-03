package clap.server.application.port.inbound.task;

public interface TerminateTaskUsecase {
    void terminateTask(Long memberId, Long taskId, String reason);
}
