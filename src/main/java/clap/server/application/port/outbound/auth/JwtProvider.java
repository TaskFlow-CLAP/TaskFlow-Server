package clap.server.application.port.outbound.auth;

import clap.server.adapter.outbound.jwt.JwtClaims;
import clap.server.common.AuthConstants;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public interface JwtProvider {
    default String resolveToken(String header) {
        if (StringUtils.hasText(header) && header.startsWith(AuthConstants.TOKEN_PREFIX.getValue())) {
            return header.substring(AuthConstants.TOKEN_PREFIX.getValue().length());
        }
        return "";
    }

    String createToken(JwtClaims claims);

    JwtClaims parseJwtClaimsFromToken(String token);

    LocalDateTime getExpiredDate(String token);

    boolean isTokenExpired(String token);

    Claims getClaimsFromToken(String token);
}
