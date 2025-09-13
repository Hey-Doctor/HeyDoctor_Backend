package HeyDoctor.HeyDoctor_Backend.domain.gemini.controller;

import HeyDoctor.HeyDoctor_Backend.domain.gemini.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/simple")
    public Mono<ResponseEntity<String>> callGemini(@RequestBody String input) {
        return geminiService.generateSimpleResponse(input)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NO_CONTENT).body("No response from Gemini"))
                .onErrorResume(e -> {
                    System.err.println("Error calling Gemini API: " + e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request: " + e.getMessage()));
                });
    }
}