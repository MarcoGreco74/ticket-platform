package it.m4.spring.ticket_platform.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.m4.spring.ticket_platform.model.Ticket;
import it.m4.spring.ticket_platform.repository.CategoriaRepository;
import it.m4.spring.ticket_platform.repository.NotaRepository;
import it.m4.spring.ticket_platform.repository.TicketRepository;


@RestController
@RequestMapping("/api/ticket/v2")
public class TicketRestAdvancedController {

    private TicketRepository ticketRepository;
    private NotaRepository notaRepository;
    private CategoriaRepository categoriaRepository;

    @Autowired
    public TicketRestAdvancedController(TicketRepository ticketRepository,NotaRepository notaRepository , CategoriaRepository categoriaRepository ){
        this.ticketRepository = ticketRepository;
        this.notaRepository = notaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> index(@RequestParam(name="keyword", required=false) String param){
        List<Ticket> ticket;
        if(param != null && !param.isBlank()){
            ticket = ticketRepository.findByTitoloContainingIgnoreCase(param);
        }else{
             ticket = ticketRepository.findAll();
        }
        //return new ResponseEntity<>(pizze, HttpStatus.OK);
        return ResponseEntity.ok().body(ticket);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> trovaxId(@PathVariable Integer id) {
        Optional<Ticket> optTicket = ticketRepository.findById(id);
        if(!optTicket.isPresent()) {
            Map<String, String> msg = new HashMap<>();
            msg.put("errore", "Ticket con ID " + id + " non trovato");
            return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(msg);
        }else{
            return new ResponseEntity<>(optTicket.get(), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletexId(@PathVariable Integer id){
        ticketRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Ticket> create(@RequestBody Ticket entity) {
        Ticket ticket = ticketRepository.save(entity);
        return new ResponseEntity<>(ticket, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> update(@PathVariable Integer id, @RequestBody Ticket entity) {
        try {
            Ticket ticketUpdated = ticketRepository.save(entity);
            return new ResponseEntity<>(ticketUpdated, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
