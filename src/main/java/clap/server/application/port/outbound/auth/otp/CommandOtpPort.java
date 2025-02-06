package clap.server.application.port.outbound.auth.otp;

import clap.server.domain.model.auth.Otp;

public interface CommandOtpPort {
    void save(Otp otp);
    void deleteByEmail(String email);
}
