package it.m4.spring.ticket_platform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.m4.spring.ticket_platform.model.Role;
import it.m4.spring.ticket_platform.model.User;
import it.m4.spring.ticket_platform.model.UserDto;
import it.m4.spring.ticket_platform.repository.RoleRepository;
import it.m4.spring.ticket_platform.repository.UserRepository;

@Service
@Transactional
public class RegisterUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void registerNewUserAccount(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new RuntimeException("There is an account with that email address: "+ userDto.getUsername());
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());

        /*Optional<Role> optionalRole = roleRepository.findByNome(userDto.getRole());
            if (!optionalRole.isPresent()) {
                throw new RuntimeException("Default role not found");
            }
        Role userRole = optionalRole.get();*/

        Role userRole = roleRepository.findByNome(userDto.getRole())
                     .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.getRole().add(userRole);
        userRepository.saveAndFlush(user);
    }

}
