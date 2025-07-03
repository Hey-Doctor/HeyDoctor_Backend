package HeyDoctor.HeyDoctor_Backend.domain.member.service;

import HeyDoctor.HeyDoctor_Backend.api.dto.MemberResponseDto;
import HeyDoctor.HeyDoctor_Backend.domain.member.model.Member;
import HeyDoctor.HeyDoctor_Backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        return new MemberResponseDto(member.getId(), member.getName(), member.getEmail());
    }
}
