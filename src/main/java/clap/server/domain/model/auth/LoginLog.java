package clap.server.domain.model.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginLog {
	private String sessionId;
	private String clientIp;
	private String attemptNickname;
	private LocalDateTime lastAttemptAt;
	private int attemptCount;
	private boolean isLocked;

	@Builder
	private LoginLog(String sessionId, String clientIp, String attemptNickname, LocalDateTime lastAttemptAt,
					 int attemptCount, boolean isLocked) {
		this.sessionId = sessionId;
		this.clientIp = clientIp;
		this.attemptNickname = attemptNickname;
		this.lastAttemptAt = lastAttemptAt;
		this.attemptCount = attemptCount;
		this.isLocked = isLocked;
	}

	public static LoginLog createLoginLog(String sessionId, String clientIp, String attemptNickname) {
		return LoginLog.builder()
				.sessionId(sessionId)
				.clientIp(clientIp)
				.attemptNickname(attemptNickname)
				.lastAttemptAt(LocalDateTime.now())
				.attemptCount(1)
				.isLocked(false)
				.build();
	}

	public int recordFailedAttempt() {
		this.attemptCount++;
		return this.attemptCount;
	}

	public void setLocked(boolean locked) {
		isLocked = locked;
	}
}
