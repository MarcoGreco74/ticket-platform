package it.m4.spring.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.m4.spring.ticket_platform.model.Ticket;
import it.m4.spring.ticket_platform.model.User;
import it.m4.spring.ticket_platform.repository.UserRepository;
import it.m4.spring.ticket_platform.security.DatabaseUserDetails;
import it.m4.spring.ticket_platform.service.TicketService;
import it.m4.spring.ticket_platform.service.UserService;


@Controller
@RequestMapping("/user")
public class UserDisponibileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @PostMapping("/operatoreDisponibilita")
    public String editDisp(
        @ModelAttribute("user") User formUser, @AuthenticationPrincipal DatabaseUserDetails userDetails, RedirectAttributes redirectAttributes) {
        Integer userId = userDetails.getId(); // id che mi porto nel topbar in input hidden
        Boolean nuovoStato = formUser.getDisponibile(); // true = disponibile, false = non disponibile
        User user = userService.findById(userId);
        // Se l'utente vuole diventare DISPONIBILE (true)
        if (nuovoStato) {
            List<Ticket> attivi = ticketService.findByOperatoreAndStatoIn(
            user, List.of("DA_FARE", "IN_CORSO")
        );
        if (!attivi.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hai ticket attivi, non puoi diventare disponibile.");
            return "redirect:/ticket";
        }
    }
    // Aggiorno lo stato normalmente
    userRepository.updateDisponibilitaById(userId, nuovoStato);
    redirectAttributes.addFlashAttribute("successMessage", "Stato aggiornato.");
        return "redirect:/ticket";
    }

}

