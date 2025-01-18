package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;
import clap.server.domain.model.member.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberPersistenceMapper extends PersistenceMapper<MemberEntity, Member> {
}
