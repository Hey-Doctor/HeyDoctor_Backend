package HeyDoctor.HeyDoctor_Backend.api.controller;

import HeyDoctor.HeyDoctor_Backend.api.dto.MemberResponseDto;
import HeyDoctor.HeyDoctor_Backend.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }
}
