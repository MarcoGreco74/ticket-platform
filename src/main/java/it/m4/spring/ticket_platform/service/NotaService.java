package it.m4.spring.ticket_platform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.m4.spring.ticket_platform.model.Nota;
import it.m4.spring.ticket_platform.repository.NotaRepository;

@Service
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;

    public List<Nota> findByTicketId(Integer ticketId) {
        return notaRepository.findByTicketId(ticketId);
    }

    public Nota save(Nota nota) {
        return notaRepository.save(nota);
    }

    public Optional<Nota> findById(Integer id) {
        return notaRepository.findById(id);
    }
}



