package clap.server.domain.model.auth;

public record ForbiddenToken(String accessToken, Long userId, long ttl) {

	public static ForbiddenToken of(String accessToken, Long userId, long ttl) {
		return new ForbiddenToken(accessToken, userId, ttl);
	}
}