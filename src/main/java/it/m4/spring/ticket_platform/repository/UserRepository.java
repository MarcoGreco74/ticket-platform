package it.m4.spring.ticket_platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.m4.spring.ticket_platform.model.User;
import jakarta.transaction.Transactional;


public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email); 

    @Query("SELECT u FROM User u JOIN u.role r WHERE r.nome = :role") 
    List<User> findByRole(@Param("role") String role); // si può eliminare e sostituire con List<User> findByRoleNome(String role);

    @Query("SELECT u FROM User u JOIN u.role r WHERE r.nome = 'OPERATORE' AND u.id = :id")
    Optional<User> findOperatoreById(@Param("id") Integer id);

    public Optional<User> findByUsername(String username);

    List<User> findByRoleNomeAndDisponibileTrue(String role);

    List<User> findByRoleNome(String role);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.disponibile = :disponibile WHERE u.id = :id")
    void updateDisponibilitaById(@Param("id") Integer id, @Param("disponibile") boolean disponibile);

    @Query("SELECT u FROM User u JOIN u.role r WHERE r.nome = 'OPERATORE'")
    List<User> findAllOperatori(); // si può eliminare e sostituire con List<User> findByRoleNome(String nome);

}

