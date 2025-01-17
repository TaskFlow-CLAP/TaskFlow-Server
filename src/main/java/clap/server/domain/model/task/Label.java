package clap.server.domain.model.task;

import clap.server.adapter.outbound.persistense.entity.task.LabelEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.LabelStatus;
import clap.server.domain.model.member.Member;

public class Label extends LabelEntity {
    private Long labelId;
    private Member admin;
    private String name;
    private LabelStatus labelStatus;
}
