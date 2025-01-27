package clap.server.domain.model.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
	private Long memberId;
	private String token;
	private  long ttl;

	@Builder
	private RefreshToken(Long memberId, String token, long ttl) {
		this.memberId = memberId;
		this.token = token;
		this.ttl = ttl;
	}

	public static RefreshToken of(Long memberId, String token, long ttl) {
		return RefreshToken.builder()
			.memberId(memberId)
			.token(token)
			.ttl(ttl)
            .build();
	}

	public void rotation(String token) {
		this.token = token;
	}

	public static boolean isTakenAway(String requestRefreshToken, String expectedRefreshToken) {
		return !requestRefreshToken.equals(expectedRefreshToken);
	}
}
