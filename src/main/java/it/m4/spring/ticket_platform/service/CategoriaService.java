package it.m4.spring.ticket_platform.service;

import java.util.List;

import org.springframework.stereotype.Service;

import it.m4.spring.ticket_platform.model.Categoria;
import it.m4.spring.ticket_platform.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria findById(Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void deleteById(Integer id) {
        categoriaRepository.deleteById(id);
    }

    public Categoria findByNomeIgnoreCase(String nome){
        return categoriaRepository.findByNomeIgnoreCase(nome);
    }
}

