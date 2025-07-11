package HeyDoctor.HeyDoctor_Backend.domain.users.service;

import HeyDoctor.HeyDoctor_Backend.domain.users.dto.UserResponse;
import HeyDoctor.HeyDoctor_Backend.domain.users.model.User;
import HeyDoctor.HeyDoctor_Backend.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository memberRepository;

    @Override
    public UserResponse getMemberById(Long id) {
        User user = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
