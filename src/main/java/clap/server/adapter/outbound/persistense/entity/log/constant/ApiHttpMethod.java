package clap.server.adapter.outbound.persistense.entity.log.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiHttpMethod {
    POST,
    GET,
    PATCH,
    PUT,
    DELETE,
    UNKNOWN; // Logging
}
