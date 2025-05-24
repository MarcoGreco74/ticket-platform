package it.m4.spring.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.m4.spring.ticket_platform.model.Categoria;
import it.m4.spring.ticket_platform.model.Ticket;
import it.m4.spring.ticket_platform.model.User;
import it.m4.spring.ticket_platform.security.DatabaseUserDetails;
import it.m4.spring.ticket_platform.service.CategoriaService;
import it.m4.spring.ticket_platform.service.TicketService;
import it.m4.spring.ticket_platform.service.UserService;


@Controller
@RequestMapping("/categorie")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public String listaCategorie(Model model, @AuthenticationPrincipal DatabaseUserDetails userDetails) {
        List<Categoria> categorie = categoriaService.findAll();
        User user = userService.findById(userDetails.getId()); // prendo user id perché nella topbar è richiesto nell'input hidden
        model.addAttribute("user", user);
        model.addAttribute("lista", categorie);
        return "categoria/categorie";
    }

    @GetMapping("/{id}") 
    public String visualizzaPerCategoria(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal DatabaseUserDetails userDetails) {
        Categoria categoria = categoriaService.findById(id);
        User user = userService.findById(userDetails.getId());
        if (categoria == null) {
            return "redirect:/errore"; 
        }
        List<Ticket> tickets = ticketService.findByCategoria(categoria);
        model.addAttribute("categoria", categoria);
        model.addAttribute("categorie", categoriaService.findAll());
        model.addAttribute("tickets", tickets);
        model.addAttribute("user", user);
        return "categoria/categorieTickets"; 
    }
}

