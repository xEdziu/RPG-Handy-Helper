package dev.goral.rpghandyhelper.user.additional;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/forgotPassword")
public class ForgotPasswordController {
        ForgotPasswordService forgotPasswordService;

        @PutMapping(path="/reset")
        public Map<String, Object> resetPassword(@RequestBody Map<String, String> email){
            return forgotPasswordService.resetPassword(email);
        }

        @GetMapping(path="/validateToken")
        public Map<String, Object> validateToken(@RequestParam("token") String token){
            return forgotPasswordService.validateToken(token);
        }

        @PutMapping(path="/changePassword")
        public Map<String, Object> changePassword(@RequestParam("token") String token, @RequestBody Map<String, String> password){
            return forgotPasswordService.changePassword(token, password);
        }

}
