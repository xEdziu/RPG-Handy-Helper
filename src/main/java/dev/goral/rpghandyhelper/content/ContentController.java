package dev.goral.rpghandyhelper.content;

import dev.goral.rpghandyhelper.user.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

    @GetMapping("/")
    public String getIndex() {
        System.out.println("ContentController: getIndex()");
        return "index";
    }

    @GetMapping("/demo")
    public String getDemo() {
        System.out.println("ContentController: getDemo()");
        return "demo";
    }

    @GetMapping("/login")
    public String getLogin() {
        Authentication authentication = getAuthentication();

        if (authentication.isAuthenticated() && authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRole.ROLE_ADMIN.name()))) {
            System.out.println("ContentController.getLogin -> admin");
            return "redirect:/admin";
        } else if (authentication.isAuthenticated() && authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRole.ROLE_USER.name()))) {
            System.out.println("ContentController.getLogin -> home");
            return "redirect:/home";
        }

        System.out.println("ContentController.getLogin -> login");
        return "auth/login";
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
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

    @GetMapping("/activate")
    public String getActivate() {
        System.out.println("ContentController: getActivate()");
        return "auth/activate";
    }

    @GetMapping("/forgotPassword")
    public String getForgotPassword() {
        System.out.println("ContentController: getForgotPassword()");
        return "auth/forgotPassword";
    }

    @GetMapping("/resetPassword")
    public String getResetPassword() {
        System.out.println("ContentController: getResetPassword()");
        return "auth/resetPassword";
    }

    @GetMapping("/profile")
    public String getProfile() {
        System.out.println("ContentController: getProfile()");
        return "home/profile";
    }

    @GetMapping("/home/notes")
    public String getNotes() {
        System.out.println("ContentController: getNotes()");
        return "home/notes";
    }

    // ADMIN PAGES

    @GetMapping("/admin")
    public String getAdmin() {
        System.out.println("ContentController: getAdmin()");
        return "admin/admin";
    }

    @GetMapping("/admin/users")
    public String getAdminUsers() {
        System.out.println("ContentController: getAdminUsers()");
        return "admin/users";
    }

    @GetMapping("/admin/user/edit")
    public String getAdminUserEdit() {
        System.out.println("ContentController: getAdminUserEdit()");
        return "admin/userEdit";
    }

    @GetMapping("/admin/user/myPassword")
    public String getAdminUserMyPassword() {
        System.out.println("ContentController: getAdminUserMyPassword()");
        return "admin/changePasswordAdmin";
    }
}
