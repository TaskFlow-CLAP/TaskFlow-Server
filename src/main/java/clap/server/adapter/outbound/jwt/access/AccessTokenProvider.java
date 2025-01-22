package clap.server.adapter.outbound.jwt.access;

import clap.server.adapter.outbound.jwt.JwtClaims;
import clap.server.application.port.outbound.jwt.JwtProvider;
import clap.server.exception.JwtException;
import clap.server.common.annotation.jwt.AccessTokenStrategy;
import clap.server.common.utils.DateUtil;
import clap.server.exception.code.AuthErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static clap.server.adapter.outbound.jwt.access.AccessTokenClaimKeys.USER_ID;

@Slf4j
@Primary
@Component
@AccessTokenStrategy
public class AccessTokenProvider implements JwtProvider {
    private final SecretKey secretKey;
    private final Duration tokenExpiration;

    public AccessTokenProvider(
            @Value("${jwt.secret-key.access-token}") String jwtSecretKey,
            @Value("${jwt.expiration-time.access-token}") Duration tokenExpiration
    ) {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.tokenExpiration = tokenExpiration;
    }

    @Override
    public String createToken(JwtClaims claims) {
        Date now = new Date();

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(claims.getClaims())
                .signWith(secretKey)
                .setExpiration(createExpirationDate(now, tokenExpiration.toMillis()))
                .compact();
    }

    @Override
    public JwtClaims parseJwtClaimsFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return AccessTokenClaim.of(
                Long.parseLong(claims.get(USER_ID.getValue(), String.class))
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
            throw new JwtException(AuthErrorCode.EMPTY_ACCESS_KEY);
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
            log.warn("Token is invalid: {}", e.getMessage());
            throw new JwtException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    private Map<String, Object> createHeader() {
        return Map.of(
                "typ", "JWT",
                "alg", "HS256",
                "regDate", System.currentTimeMillis()
        );
    }

    private Date createExpirationDate(Date now, long expirationTime) {
        return new Date(now.getTime() + expirationTime);
    }
}
