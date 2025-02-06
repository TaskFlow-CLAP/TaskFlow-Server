package clap.server.application.port.outbound.auth.otp;

import clap.server.domain.model.auth.Otp;

import java.util.Optional;

public interface LoadOtpPort {
    Optional<Otp> findByEmail(String email);
}
