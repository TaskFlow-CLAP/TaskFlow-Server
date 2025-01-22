package clap.server.adapter.outbound.jwt;

import java.util.Map;

public interface JwtClaims {
    Map<String, Object> getClaims();
}
