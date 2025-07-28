package HeyDoctor.HeyDoctor_Backend.domain.users.model.signup;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class EnvFileLoadTest {

    private static final Logger logger = LoggerFactory.getLogger(EnvFileLoadTest.class);

    @Autowired
    private Environment env;

    private void checkVariables(List<String> keys) {
        List<String> missingKeys = new ArrayList<>();

        for (String key : keys) {
            String value = env.getProperty(key);
            if (value == null || value.isBlank()) {
                missingKeys.add(key);
                logger.error("Environment variable '{}' 불러오기 실패", key);
            } else {
                logger.info("Environment variable '{}' 불러오기 성공: {}", key, value);
            }
        }

        // AssertJ 이용해서 누락된 키가 없음을 검증
        assertThat(missingKeys)
                .withFailMessage("다음 환경변수들이 누락되었습니다: %s", missingKeys)
                .isEmpty();
    }

    @Test
    @Order(1)
    void testDatasourceVariables() {
        logger.info("=== 테스트 1: Datasource 환경변수 체크 시작 ===");
        checkVariables(List.of(
                "SPRING_DATASOURCE_URL",
                "SPRING_DATASOURCE_USERNAME",
                "SPRING_DATASOURCE_PASSWORD"
        ));
        logger.info("=== 테스트 1 완료 ===");
    }

    @Test
    @Order(2)
    void testOauthGoogleVariables() {
        logger.info("=== 테스트 2: OAuth Google 환경변수 체크 시작 ===");
        checkVariables(List.of(
                "OAUTH_GOOGLE_CLIENT_ID",
                "OAUTH_GOOGLE_CLIENT_SECRET",
                "OAUTH_GOOGLE_REDIRECT_URI"
        ));
        logger.info("=== 테스트 2 완료 ===");
    }

    @Test
    @Order(3)
    void testOauthKakaoVariables() {
        logger.info("=== 테스트 3: OAuth Kakao 환경변수 체크 시작 ===");
        checkVariables(List.of(
                "OAUTH_KAKAO_CLIENT_ID",
                "OAUTH_KAKAO_CLIENT_SECRET",
                "OAUTH_KAKAO_REDIRECT_URI"
        ));
        logger.info("=== 테스트 3 완료 ===");
    }

    @Test
    @Order(4)
    void testOauthNaverVariables() {
        logger.info("=== 테스트 4: OAuth Naver 환경변수 체크 시작 ===");
        checkVariables(List.of(
                "OAUTH_NAVER_CLIENT_ID",
                "OAUTH_NAVER_CLIENT_SECRET",
                "OAUTH_NAVER_REDIRECT_URI"
        ));
        logger.info("=== 테스트 4 완료 ===");
    }

    @Test
    @Order(5)
    void testJwtVariables() {
        logger.info("=== 테스트 5: JWT 환경변수 체크 시작 ===");
        checkVariables(List.of(
                "JWT_SECRET",
                "JWT_REDIRECT_URI",
                "ACCESS_TOKEN_EXPIRATION_TIME",
                "REFRESH_TOKEN_EXPIRATION_TIME"
        ));
        logger.info("=== 테스트 5 완료 ===");
    }
}
