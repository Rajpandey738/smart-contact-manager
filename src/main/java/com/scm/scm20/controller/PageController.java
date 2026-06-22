package com.scm.scm20.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.scm.scm20.entities.User;
import com.scm.scm20.forms.UserForm;
import com.scm.scm20.services.UserService;

import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("currentPage", "home");
        model.addAttribute("showNavbar", true);
        System.out.println("Home page accessed");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("currentPage", "about");
        model.addAttribute("showNavbar", true);
        System.out.println("About page accessed");
        return "about";
    }

    @RequestMapping("/services")
    public String services(Model model) {
        model.addAttribute("currentPage", "services");
        model.addAttribute("showNavbar", true);
        System.out.println("Services page accessed");
        return "services";
    }

    @RequestMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("currentPage", "contact");
        model.addAttribute("showNavbar", true);
        System.out.println("Contact page accessed");
        return "contact";
    }

    @GetMapping("/register")
    public String register(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        model.addAttribute("showNavbar", false);
        return "register";
    }

    @PostMapping("/do-register")
    public String doRegister(@Valid @ModelAttribute UserForm userFrom, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        // Handle registration logic here

        // Validation check
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // userservice
        // User user = User.builder()
        // .username(userFrom.getUsername())
        // .email(userFrom.getEmail())
        // .password(userFrom.getPassword())
        // .build();

        // try {
        User user = new User();
        user.setName(userFrom.getName());
        user.setEmail(userFrom.getEmail());
        user.setPassword(userFrom.getPassword());

        userService.saveUser(user);

        // Message
        redirectAttributes.addFlashAttribute("message", "Account created successfully.");
        redirectAttributes.addFlashAttribute("messageType", "green");
        // }
        // catch (Exception e) {
        // redirectAttributes.addFlashAttribute("message", "Try after some time");
        // redirectAttributes.addFlashAttribute("messageType", "red");
        // }

        return "redirect:/login";
    }

    // Remove Session Message
    // @GetMapping("/remove-message")
    // @ResponseBody
    // public void removeMessage(HttpSession session) {

    // session.removeAttribute("message");
    // session.removeAttribute("messageType");
    // }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("showNavbar", false);
        return "login";
    }

}