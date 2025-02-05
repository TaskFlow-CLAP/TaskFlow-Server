package clap.server.adapter.inbound.web.dto.admin.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record AddMainCategoryRequest(
        @NotBlank @Length(max = 20)
        String name,
        @NotBlank @Pattern(regexp = "^[A-Z]{1,2}$", message = "올바른 카테고리 코드 형식이 아닙니다.")
        String code
) {
}