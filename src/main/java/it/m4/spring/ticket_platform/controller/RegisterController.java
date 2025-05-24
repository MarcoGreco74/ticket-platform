package it.m4.spring.ticket_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.m4.spring.ticket_platform.model.UserDto;
import it.m4.spring.ticket_platform.repository.RoleRepository;
import it.m4.spring.ticket_platform.security.RegisterUserService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterUserService registerUserService;

   @Autowired
    private RoleRepository roleRepository;
   
    @GetMapping
    public String displayForm(Model model) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("role", roleRepository.findAll());
        return "register";
    }

    @PostMapping
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto, Model model, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return "register";  
            }
            registerUserService.registerNewUserAccount(userDto);
            model.addAttribute("successMessage", "Registrazione completata!");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("role", roleRepository.findAll());
            return "register";
        }
        return "redirect:/login";
    }
}
