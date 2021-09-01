package com.epam.specimenbase.catalog.webservice.auth;

import com.epam.specimenbase.catalog.UseCaseFactory;
import com.epam.specimenbase.catalog.domain.users.GetUserPassword;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UseCaseFactory useCaseFactory;

    public UserDetailsServiceImpl(UseCaseFactory useCaseFactory) {
        this.useCaseFactory = useCaseFactory;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            String password = useCaseFactory.getUserPassword().getUserPassword(email);
            return new User(email, password, List.of(new SimpleGrantedAuthority("USER")));
        } catch (GetUserPassword.UserNotFoundException e) {
            throw new UsernameNotFoundException("User " + email + " not found");
        }
    }
}
