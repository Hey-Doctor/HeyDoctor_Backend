package HeyDoctor.HeyDoctor_Backend.domain.member.service;

import HeyDoctor.HeyDoctor_Backend.api.dto.MemberResponseDto;

public interface MemberService {
    MemberResponseDto getMemberById(Long id);
}
