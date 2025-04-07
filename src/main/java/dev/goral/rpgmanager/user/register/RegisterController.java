package dev.goral.rpgmanager.user.register;

import dev.goral.rpgmanager.security.recaptcha.RecaptchaService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/register")
@AllArgsConstructor
public class RegisterController {

    private RegisterService registerService;
    private final RecaptchaService recaptchaService;

    @PostMapping(path="/signup")
    public Map<String, Object> register(@RequestBody RegisterRequest request){
        System.out.println(request);
        if (!recaptchaService.verifyRecaptcha(request.getCaptcha())) {
            throw new IllegalStateException("Recaptcha verification failed");
        }
        return registerService.register(request);
    }

    @GetMapping(path="/confirm")
    public Map<String, Object> confirm(@RequestParam("token") String token){
        return registerService.confirmEmail(token);
    }

    @GetMapping(path = "/checkUsername")
    public Map<String, Object> checkUsername(@RequestParam("username") String username){
        return registerService.checkUsernameAvailability(username);
    }

    @GetMapping(path = "/checkEmail")
    public Map<String, Object> checkEmail(@RequestParam("email") String email){
        return registerService.checkEmailAvailability(email);
    }
}