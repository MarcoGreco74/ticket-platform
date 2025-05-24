package it.m4.spring.ticket_platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.m4.spring.ticket_platform.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByNome(String nome);
}


