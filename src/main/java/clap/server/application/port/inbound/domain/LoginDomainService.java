package clap.server.application.port.inbound.domain;

import org.springframework.stereotype.Service;

@Service
public class LoginDomainService {

    public int getFailedAttemptCount(String loginNickname) {
        //TODO: 로그인 실패 횟수 계산 로직 추가
        return 3;
    }
}
