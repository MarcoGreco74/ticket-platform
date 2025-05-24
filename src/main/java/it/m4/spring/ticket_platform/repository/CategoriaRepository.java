package it.m4.spring.ticket_platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.m4.spring.ticket_platform.model.Categoria;


public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    Optional<Categoria> findByNome(String nome); // va bene uno solo dei due

    Categoria findByNomeIgnoreCase(String nome);

}

