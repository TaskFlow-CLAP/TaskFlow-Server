package clap.server.domain.model.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.StatusFlag;
import clap.server.domain.model.common.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Status extends BaseTime {
    private Long statusId;
    private Long adminId;
    private String code;
    private String name;
    public StatusFlag statusFlag;

 }
