package HeyDoctor.HeyDoctor_Backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // API 경로에만 CORS 설정
                .allowedOrigins("http://localhost:5173") // React 개발 서버
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용 메서드
                .allowedHeaders("*")  // 모든 요청 헤더 허용
                .allowCredentials(true)  // 인증 정보 허용 (쿠키, 세션 등)
                .maxAge(3600);  // 1시간 동안 preflight 요청 결과 캐시
    }
}
