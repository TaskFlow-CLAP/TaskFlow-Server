package clap.server.adapter.outbound.jwt.refresh;

import clap.server.adapter.outbound.jwt.JwtClaims;
import clap.server.application.port.outbound.auth.JwtProvider;
import clap.server.common.annotation.jwt.RefreshTokenStrategy;
import clap.server.common.utils.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
@RefreshTokenStrategy
public class RefreshTokenProvider implements JwtProvider {
    private final SecretKey secretKey;
    private final Long tokenExpiration;

    public RefreshTokenProvider(
            @Value("${jwt.secret-key.refresh-token}") String jwtSecretKey,
            @Value("${jwt.expiration-time.refresh-token}") Long tokenExpiration
    ) {
        final byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecretKey);
        this.secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
        this.tokenExpiration = tokenExpiration;
    }

    @Override
    public String createToken(JwtClaims claims) {
        Date now = new Date();

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(claims.getClaims())
                .signWith(secretKey)
                .setExpiration(createExpireDate(now, tokenExpiration))
                .compact();
    }

    @Override
    public JwtClaims parseJwtClaimsFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return RefreshTokenClaim.of(
                Long.parseLong(claims.get(RefreshTokenClaimKeys.USER_ID.getValue()).toString())
        );
    }

    @Override
    public LocalDateTime getExpiredDate(String token) {
        Claims claims = getClaimsFromToken(token);
        return DateUtil.toLocalDateTime(claims.getExpiration());
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            log.error("Token is expired: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Token parsing error: {}", e.getMessage());
            throw e;
        }
    }

    private Map<String, Object> createHeader() {
        return Map.of(
                "typ", "JWT",
                "alg", "HS256",
                "regDate", System.currentTimeMillis()
        );
    }

    private Date createExpireDate(final Date now, long expirationTime) {
        return new Date(now.getTime() + expirationTime);
    }
}
