package clap.server.application.port.inbound.domain;

import org.springframework.stereotype.Service;

// TODO: 로그인 도메인 실패횟수 예제 코드 (나중에 삭제할 예정)
@Service
public class LoginDomainService {

    public Integer getFailedAttemptCount(String loginNickname) {
        // 로그인 실패 횟수 계산 로직 추가
        return 3; // 테스트값
    }
}
