package clap.server.adapter.inbound.web.dto.common;


import java.util.List;

public record SliceResponse<T> (
        List<T> content,
        boolean isFirst,
        boolean isLast
) {
}
