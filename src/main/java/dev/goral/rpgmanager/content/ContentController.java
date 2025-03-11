package dev.goral.rpgmanager.content;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

    @GetMapping("/")
    public String getIndex() {
        System.out.println("ContentController: getIndex()");
        return "index";
    }

    @GetMapping("/login")
    public String getLogin() {
        System.out.println("ContentController: getLogin()");
        return "auth/login";
    }

    @GetMapping("/home")
    public String getHome() {
        System.out.println("ContentController: getHome()");
        return "home/home";
    }

    @GetMapping("/register")
    public String getRegister() {
        System.out.println("ContentController: getRegister()");
        return "auth/register";
    }
}
