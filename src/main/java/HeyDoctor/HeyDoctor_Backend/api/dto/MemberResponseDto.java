package HeyDoctor.HeyDoctor_Backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String name;
    private String email;
}
