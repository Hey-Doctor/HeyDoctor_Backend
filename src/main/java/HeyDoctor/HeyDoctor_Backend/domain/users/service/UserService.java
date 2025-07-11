package HeyDoctor.HeyDoctor_Backend.domain.users.service;

import HeyDoctor.HeyDoctor_Backend.domain.users.dto.UserResponse;

public interface UserService {
    UserResponse getMemberById(Long id);
}
