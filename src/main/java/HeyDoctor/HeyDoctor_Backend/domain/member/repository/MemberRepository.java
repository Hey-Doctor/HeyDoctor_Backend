package HeyDoctor.HeyDoctor_Backend.domain.member.repository;

import HeyDoctor.HeyDoctor_Backend.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
