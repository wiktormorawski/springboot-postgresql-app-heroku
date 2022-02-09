package pl.ug.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.ug.project.domain.Search;
import pl.ug.project.domain.User;
import pl.ug.project.services.UserService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.util.Date;

@Slf4j
@Controller
public class AuthController {
    @Autowired
    JavaMailSender emailSender;
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String loginPage(Model model, Search search, String message){
        model.addAttribute("search", search);
        model.addAttribute("message", message);
        return "loginPage";
    }
    @GetMapping("/register")
    public String registerPage(Model model, Search search, String message){
        model.addAttribute("search", search);
        model.addAttribute("message", message);
        model.addAttribute("user", new User());
        return "registerPage";
    }
    @PostMapping("/register")
    public String registerUser(Model model, Search search, String message, RedirectAttributes redirectAttributes, @Valid User user, Errors errors) throws MessagingException {
        model.addAttribute("search", search);
        model.addAttribute("message", message);
        if(errors.hasErrors()){
            model.addAttribute("message", "Register failed, try again :(");
            model.addAttribute("search", new Search());
            return "registerPage";
        }
        User createdUser = userService.registerNewUser(user, "ROLE_USER");
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(createdUser.getEmail());
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMsg = "<div><h2>User registered</h2><h1>Name: %s</h1><h2>Date: %s</h2><h2>Login: %s</h2><h2>THANKS, it means a lot for us.</h2></div>".formatted(createdUser.getName() + createdUser.getSurname(), new Date(), createdUser.getUserName());
        helper.setText(htmlMsg, true);
        helper.setTo(createdUser.getEmail());
        helper.setSubject("You Successfully register in Wiktor's Blog " + createdUser.getName() + " " + createdUser.getSurname() + "  CONGRATS :)");
        helper.setFrom("wiktorMorawskiSpring@fakegmail.com");
        emailSender.send(mimeMessage);
        redirectAttributes.addFlashAttribute("message", "Register ended successfully, please log in.");
        return "redirect:/";
    }
}
