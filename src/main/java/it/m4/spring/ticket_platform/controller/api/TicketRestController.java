package it.m4.spring.ticket_platform.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.m4.spring.ticket_platform.model.Categoria;
import it.m4.spring.ticket_platform.model.Ticket;
import it.m4.spring.ticket_platform.repository.CategoriaRepository;
import it.m4.spring.ticket_platform.repository.TicketRepository;
import it.m4.spring.ticket_platform.service.TicketService;


@RestController
@RequestMapping("/api/ticket")
public class TicketRestController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TicketRepository ticketRepository;

    //Tutti i ticket
    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.findAll();
    }

    //Filtra per categoria (es. /api/ticket?categoria=junior)
    @GetMapping("/categoria")
    public ResponseEntity<List<Ticket>> perCategoria(@RequestParam String nome) {
    Categoria categoria = categoriaRepository.findByNomeIgnoreCase(nome);
    if (categoria == null) {
        return ResponseEntity.badRequest().build(); // categoria non trovata
    }
    List<Ticket> tickets = ticketRepository.findByCategoria(categoria);
    return ResponseEntity.ok(tickets);
    }

}

