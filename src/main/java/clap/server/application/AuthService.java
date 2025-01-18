package clap.server.application;

import clap.server.application.port.inbound.auth.AuthUsecase;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class AuthService implements AuthUsecase {
}
