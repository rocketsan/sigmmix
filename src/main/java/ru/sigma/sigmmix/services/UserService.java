package ru.sigma.sigmmix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.sigma.sigmmix.model.User;
import ru.sigma.sigmmix.repositories.UserRepository;

import java.util.Collections;

@Component
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Функция проверкаи авторизации
     * @param username логин авторизирцемого пользователя
     * @return principal
     * @throws UsernameNotFoundException - если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Load user: "+username);
        User myUser = repository.findByLogin(username);
        System.out.println("Found User: "+myUser);
        GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_"+myUser.getRole().name());
        return new org.springframework.security.core.userdetails.User(myUser.getLogin(), myUser.getPassword(), Collections.singleton(auth));
    }
}
