package it.m4.spring.ticket_platform.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.m4.spring.ticket_platform.model.Nota;
import it.m4.spring.ticket_platform.model.Ticket;
import it.m4.spring.ticket_platform.model.User;
import it.m4.spring.ticket_platform.repository.NotaRepository;
import it.m4.spring.ticket_platform.repository.TicketRepository;
import it.m4.spring.ticket_platform.security.DatabaseUserDetails;
import it.m4.spring.ticket_platform.service.TicketService;
import it.m4.spring.ticket_platform.service.UserService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/note") 
public class NotaController {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    //Preparo il form per aggiungere una nota a un ticket. Passo alla view note/modifica.
    @GetMapping("/{id}/nota")  
    public String note(@PathVariable Integer id, Model model, @AuthenticationPrincipal DatabaseUserDetails userDetails) {
        Nota nota = new Nota();
        User user = userService.findById(userDetails.getId());
        nota.setTicket(ticketRepository.findById(id).get());
        model.addAttribute("nuovaNota", nota);
        model.addAttribute("editMode", false);
        model.addAttribute("user", user);
        return "note/modifica";
    }

    // POST - Salva una nuova nota associata al ticket
    @PostMapping("/create")
    public String creaNota(@ModelAttribute("nuovaNota") Nota formNota, @AuthenticationPrincipal DatabaseUserDetails userDetails, RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getId());
        formNota.setUser(user);
        formNota.setDataCreazione(LocalDate.now());
        notaRepository.save(formNota);
        redirectAttributes.addFlashAttribute("successMessage", "Nota salvata con successo.");
        return "redirect:/ticket";
    }

    // GET - Mostra tutte le note di un ticket + form per nuova nota
    @GetMapping("/modifica/{id}")
    public String listaNote(@PathVariable Integer ticketId, Model model, @AuthenticationPrincipal DatabaseUserDetails userDetails) {
        Ticket ticket = ticketService.findById(ticketId);
        List<Nota> note = notaRepository.findByTicket(ticket);
        Nota nuovaNota = new Nota();
        nuovaNota.setTicket(ticket);
        model.addAttribute("note", note);
        model.addAttribute("nuovaNota", nuovaNota);
        model.addAttribute("tckt", ticket);
        return "note/lista_note";
    }

    @PostMapping("/modifica/{id}")
    public String doEdit(@Valid @ModelAttribute("nota") Nota nota,
    BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("editMode", true);
            model.addAttribute("nuovaNota", nota);
            return "/note/modifica";
        }
        notaRepository.save(nota);
        return "redirect:/ticket/show_id/"+nota.getTicket().getId();
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        notaRepository.deleteById(id);
        return "redirect:/ticket";
    }

}


