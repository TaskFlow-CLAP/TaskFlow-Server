package clap.server.domain.model.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginLog {
	private String clientIp;
	private String attemptNickname;
	private LocalDateTime lastAttemptAt;
	private int failedCount;
	private boolean isLocked;

	public static LoginLog createLoginLog(String clientIp, String attemptNickname) {
		return LoginLog.builder()
				.clientIp(clientIp)
				.attemptNickname(attemptNickname)
				.lastAttemptAt(LocalDateTime.now())
				.failedCount(1)
				.isLocked(false)
				.build();
	}

	public int recordFailedAttempt() {
		this.failedCount++;
		return this.failedCount;
	}

	public void setLocked(boolean locked) {
		isLocked = locked;
	}

	public String toSummaryString() {
			return "{" +
					", failedCount=" + failedCount +
					", isLocked=" + isLocked +
					'}';
	}
}
