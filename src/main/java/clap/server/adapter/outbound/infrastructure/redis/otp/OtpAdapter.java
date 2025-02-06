package clap.server.adapter.outbound.infrastructure.redis.otp;

import clap.server.application.port.outbound.auth.otp.CommandOtpPort;
import clap.server.application.port.outbound.auth.otp.LoadOtpPort;
import clap.server.common.annotation.architecture.InfrastructureAdapter;
import clap.server.domain.model.auth.Otp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@InfrastructureAdapter
@RequiredArgsConstructor
public class OtpAdapter implements LoadOtpPort, CommandOtpPort {
	private final OtpRepository otpRepository;
	private final OtpMapper otpMapper;

	@Override
	public void save(Otp otp) {
		OtpEntity refreshTokenEntity = otpMapper.toEntity(otp);
		otpRepository.save(refreshTokenEntity);
	}

	@Override
	public void deleteByEmail(String email) {
		otpRepository.deleteById(email);
	}

	@Override
	public Optional<Otp> findByEmail(String email) {
		return otpRepository.findById(email).map(otpMapper::toDomain);
	}
}
