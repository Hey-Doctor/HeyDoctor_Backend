package HeyDoctor.HeyDoctor_Backend.domain.users.model;

import HeyDoctor.HeyDoctor_Backend.domain.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SignUpTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // 테스트용 데이터 넣어도 되고, 초기화할 내용 있으면 여기서
        userRepository.deleteAllInBatch(); // 각 테스트 시작 전 DB 데이터를 모두 삭제
    }

    @AfterEach
    void tearDown() {
        // 테스트 후 정리 작업
    }

    @Test
    void dbConnectionTest() {
        User user = User.builder()
                .name("JOO")
                .email("joo@gmail.com")
                .password("joo1234!")
                .build(); // 비밀번호 컬럼이 있으면 .password()도 추가

        userRepository.save(user);

        // 조회 테스트
        List<User> users = userRepository.findAll();
        assertThat(users).isNotEmpty();
        System.out.println("DB 연결 및 저장 조회 성공, 회원 수: " + users.size());
    }

}