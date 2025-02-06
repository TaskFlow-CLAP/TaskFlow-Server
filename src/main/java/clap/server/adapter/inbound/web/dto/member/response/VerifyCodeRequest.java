
package clap.server.adapter.inbound.web.dto.member.response;

import jakarta.validation.constraints.NotBlank;

public record VerifyCodeRequest(
        @NotBlank
        String email,
        @NotBlank
        String code
) {
}
