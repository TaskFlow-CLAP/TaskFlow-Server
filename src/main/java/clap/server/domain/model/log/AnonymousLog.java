package clap.server.domain.model.log;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class AnonymousLog extends ApiLog {
    private String loginNickname;
}