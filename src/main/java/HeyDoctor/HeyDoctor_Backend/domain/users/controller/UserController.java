package HeyDoctor.HeyDoctor_Backend.domain.users.controller;

import HeyDoctor.HeyDoctor_Backend.domain.users.dto.UserResponse;
import HeyDoctor.HeyDoctor_Backend.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class UserController {

    private final UserService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }
}
