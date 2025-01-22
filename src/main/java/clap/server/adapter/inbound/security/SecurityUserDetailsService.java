package clap.server.adapter.inbound.security;

import clap.server.adapter.outbound.persistense.repository.member.MemberRepository;
import clap.server.exception.AuthException;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
    private final MemberRepository loadMemberPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadMemberPort.findById(Long.parseLong(username))
                .map(SecurityUserDetails::from)
                .orElseThrow(() -> new AuthException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}