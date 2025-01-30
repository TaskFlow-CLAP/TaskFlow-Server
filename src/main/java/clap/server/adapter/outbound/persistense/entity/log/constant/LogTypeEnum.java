package clap.server.adapter.outbound.persistense.entity.log.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogTypeEnum {
    LOGIN("로그인 로그"),
    GENERAL("일반 로그");

    private final String description;

    public static LogTypeEnum fromDescription(String description) {
        for (LogTypeEnum logType : LogTypeEnum.values()) {
            if (logType.getDescription().equals(description)) {
                return logType;
            }
        }
        throw new IllegalArgumentException("해당하는 로그 타입이 없습니다: " + description);
    }
}
