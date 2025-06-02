package dev.goral.rpghandyhelper.security.recaptcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class RecaptchaService {

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Value("${recaptcha.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate;

    public RecaptchaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean verifyRecaptcha(String token) {
        if (token == null || token.isEmpty()) {
            return false; // Token jest wymagany
        }

        // 🔹 Użycie MultiValueMap zamiast Map.of()
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("secret", secretKey);
        formData.add("response", token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 🔹 To jest kluczowe!

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<RecaptchaResponse> response = restTemplate.postForEntity(RECAPTCHA_VERIFY_URL, request, RecaptchaResponse.class);
        return response.getBody() != null
                && response.getBody().isSuccess()
                && response.getBody().getScore() >= 0.5; // 🔹 Dostosuj próg akceptacji
    }
}
