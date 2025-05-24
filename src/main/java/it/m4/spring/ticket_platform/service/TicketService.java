package it.m4.spring.ticket_platform.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import it.m4.spring.ticket_platform.model.Categoria;
import it.m4.spring.ticket_platform.model.Ticket;
import it.m4.spring.ticket_platform.model.User;
import it.m4.spring.ticket_platform.repository.CategoriaRepository;
import it.m4.spring.ticket_platform.repository.TicketRepository;
import it.m4.spring.ticket_platform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CategoriaRepository categoriaRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, CategoriaRepository categoriaRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.categoriaRepository = categoriaRepository;
        this.userRepository = userRepository;
    }

    public Ticket save(Ticket ticket) {
    return ticketRepository.save(ticket);
    }

    public List<Ticket> findAll() {
    return ticketRepository.findAll();
    }

    public List<Ticket> getAllTickets() { // uguale a findAll()
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Integer id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> findByOperatoreId(Integer id){
        return ticketRepository.findByOperatore_Id(id);
    }

    public List<Ticket> findByOperatore(User operatore) {
    return ticketRepository.findByOperatore(operatore);
    }

    public List<Ticket> findByOperatoreAndStatoIn(User operatore, List<String> stato) {
    return ticketRepository.findByOperatoreAndStatoIn(operatore, stato);
    }

    public Ticket createTicket(Ticket ticket, Integer categoriaId, Integer operatoreId, Integer adminId) { // non usato
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria non trovata"));
        User operatore = userRepository.findById(operatoreId)
                .orElseThrow(() -> new RuntimeException("Operatore non trovato"));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin non trovato"));
        ticket.setCategoria(categoria);
        ticket.setOperatore(operatore);
        ticket.setAdmin(admin);
        ticket.setDataCreazione(LocalDate.now());
        ticket.setDataAggiornamento(LocalDate.now());
        return ticketRepository.save(ticket);
    }

    public Ticket updateStato(Integer ticketId, String nuovoStato) { // non usato
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket non trovato"));
        ticket.setStato(nuovoStato);
        ticket.setDataAggiornamento(LocalDate.now());
        return ticketRepository.save(ticket);
    }

    public Ticket findById(Integer id) {
    return ticketRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Ticket non trovato con id: " + id));
    }
    public List<Ticket> findByCategoria(Categoria categoria) {
        return ticketRepository.findByCategoria(categoria);
    }
    public List<Ticket> findByStato(String stato) {
        return ticketRepository.findByStatoIgnoreCase(stato);
    }

}



