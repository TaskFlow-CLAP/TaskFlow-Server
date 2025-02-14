package clap.server.domain.policy.member;

import lombok.Getter;

@Getter
public class NicknamePolicyConstants {
    public static final String NICKNAME_REGEX = "^[a-z]{3,10}\\.[a-z]{1,5}$";
}
