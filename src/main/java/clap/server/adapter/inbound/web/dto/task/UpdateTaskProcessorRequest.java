package clap.server.adapter.inbound.web.dto.task;

import clap.server.domain.model.member.Member;

public record UpdateTaskProcessorRequest(
        Long processorId
) {
}
