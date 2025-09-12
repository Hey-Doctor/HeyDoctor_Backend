package HeyDoctor.HeyDoctor_Backend.domain.gemini.service;

import HeyDoctor.HeyDoctor_Backend.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);

    private final WebClient webClient;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public GeminiService(@Value("${gemini.api-key}") String apiKey) {
        this.apiKey = apiKey;
        this.webClient = WebClient.create("https://generativelanguage.googleapis.com");
        this.objectMapper = new ObjectMapper();
    }

    public Mono<String> generateSimpleResponse(String input) {
        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", "간단히 대답해줘:\n\n" + input))
                ))
        );

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-1.5-flash-latest:generateContent")
                        .queryParam("key", apiKey)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(rawJson -> {
                    log.info("Gemini raw response: {}", rawJson);

                    try {
                        JsonNode root = objectMapper.readTree(rawJson);
                        JsonNode textNode = root
                                .path("candidates").get(0)
                                .path("content").path("parts").get(0)
                                .path("text");
                        return Mono.just(textNode.asText(""));
                    } catch (Exception e) {
                        log.error("Failed to parse Gemini response", e);
                        return Mono.error(new RuntimeException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
                    }
                });
    }
}