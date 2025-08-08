package HeyDoctor.HeyDoctor_Backend.domain.User.Controller;

import HeyDoctor.HeyDoctor_Backend.domain.User.Dto.SocialLoginRequest;
import HeyDoctor.HeyDoctor_Backend.domain.User.Service.SocialLoginService;
import HeyDoctor.HeyDoctor_Backend.global.exception.responce.ApiResponse;
import HeyDoctor.HeyDoctor_Backend.global.exception.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    @PostMapping("/social-login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> socialLogin(@RequestBody SocialLoginRequest request) {
        Map<String, Object> result = socialLoginService.processSocialLogin(request.getProvider(), request.getToken());
        return ApiResponse.onSuccess(SuccessCode.LOGIN_SUCCESS, result);

    }
}
