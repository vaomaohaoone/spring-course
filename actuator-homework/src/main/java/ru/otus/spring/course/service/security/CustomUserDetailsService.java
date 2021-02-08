package ru.otus.spring.course.service.security;


import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.spring.course.documents.Role;
import ru.otus.spring.course.documents.User;
import ru.otus.spring.course.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByName(name);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(userOptional.get().getName())
                .password(userOptional.get().getPassword())
                .roles(userOptional.get().getRoles().stream().map(Role::getName).toArray(String[]::new))
                .build();
    }
}