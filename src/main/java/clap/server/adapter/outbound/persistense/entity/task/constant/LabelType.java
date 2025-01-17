package clap.server.adapter.outbound.persistense.entity.task.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LabelType {
    EMERGENCY("긴급"),
    NORMAL("일반"),
    REGULAR("정기");

    private final String description;
}

