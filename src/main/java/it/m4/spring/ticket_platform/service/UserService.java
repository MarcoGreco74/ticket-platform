package it.m4.spring.ticket_platform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.m4.spring.ticket_platform.model.User;
import it.m4.spring.ticket_platform.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null); // oppure lancia eccezione personalizzata
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findOperatoriDisponibili(String role) {
        return userRepository.findByRoleNomeAndDisponibileTrue(role);
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findByRole(String role) {
    return userRepository.findByRole(role);
    }

    public List<User> findAllOperatori() {
        return userRepository.findAllOperatori();
    }

}

