package clap.server.application;

import clap.server.application.port.inbound.auth.ResetPasswordUsecase;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class ResetPasswordService implements ResetPasswordUsecase {
}
