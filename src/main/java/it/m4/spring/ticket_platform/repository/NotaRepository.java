package it.m4.spring.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.m4.spring.ticket_platform.model.Nota;
import it.m4.spring.ticket_platform.model.Ticket;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Integer> {

    List<Nota> findByTicketId(Integer ticketId);

    List<Nota> findByTicketOrderByDataCreazioneDesc(Ticket ticket);

    List<Nota> findByTicketIdOrderByDataCreazioneDesc(Integer ticketId);

    List<Nota> findByTicket(Ticket ticket);

}

