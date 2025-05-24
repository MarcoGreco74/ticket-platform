package it.m4.spring.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.m4.spring.ticket_platform.model.Role;
import it.m4.spring.ticket_platform.model.User;
import it.m4.spring.ticket_platform.repository.UserRepository;
import it.m4.spring.ticket_platform.security.DatabaseUserDetails;
import it.m4.spring.ticket_platform.service.TicketService;
import it.m4.spring.ticket_platform.service.UserService;

@Controller
@RequestMapping("/operatori")
public class OperatoreController {

    private final UserService userService;

    private final TicketService ticketService;

    @Autowired
    private UserRepository userRepository;;

    public OperatoreController(UserService userService, TicketService ticketService) {
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @GetMapping
    //@PreAuthorize("hasAuthority('ADMIN')") //Accessibile solo agli admin tramite @PreAuthorize
    public String listaOperatori(@AuthenticationPrincipal DatabaseUserDetails userDetails, Model model) {
        User currentUser = userService.findById(userDetails.getId());
        // Controllo se l'utente ha il ruolo ADMIN
        boolean isAdmin = false;
        for (Role ruolo : currentUser.getRole()) {
            if (ruolo.getNome().equals("ADMIN")) {
                isAdmin = true;
                break;
            }
        }
        if (!isAdmin) {
            return "redirect:/error_page"; 
        }
        List<User> operatori = userService.findAllOperatori(); 
        model.addAttribute("operatori", operatori);
        return "operatore/operatori";
    }

    @GetMapping("/") //Pagina edit per disponibilità
    public String operatoreDisp(Model model, @AuthenticationPrincipal DatabaseUserDetails userDetails) {
    User user = userService.findById(userDetails.getId());
    model.addAttribute("user", user);
    return "user/edit";
    }

}

