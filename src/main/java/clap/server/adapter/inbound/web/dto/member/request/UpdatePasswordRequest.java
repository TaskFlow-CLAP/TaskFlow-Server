package clap.server.adapter.inbound.web.dto.member.request;

import clap.server.common.annotation.validation.password.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequest(
        @NotBlank @ValidPassword
        String password
) {
}
