package it.m4.spring.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.m4.spring.ticket_platform.model.Categoria;
import it.m4.spring.ticket_platform.model.Ticket;
import it.m4.spring.ticket_platform.model.User;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    List<Ticket> findByCategoria_Id(Integer categoriaId);

    List<Ticket> findByStato(String stato);

    List<Ticket> findByOperatore_Id(Integer operatoreId);

    List<Ticket> findByOperatore(User operatore);

    List<Ticket> findByAdmin_Id(Integer adminId);

    List<Ticket> findByTitoloContainingIgnoreCase(String titolo);

    List<Ticket> findByTitoloContainingIgnoreCaseAndOperatore(String titolo, User operatore);

    List<Ticket> findByOperatoreAndStatoIn(User operatore, List<String> stato);

    //List<Ticket> findByCategoriaIgnoreCase(Categoria categoria);

    List<Ticket> findByStatoIgnoreCase(String stato);

    List<Ticket> findByCategoria(Categoria categoria);

    @Query("SELECT t FROM Ticket t WHERE LOWER(t.titolo) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Ticket> searchByTitolo(@Param("query") String query); // si può eliminare e sostituire con List<Ticket> findByTitoloContainingIgnoreCase(String titolo)
}

