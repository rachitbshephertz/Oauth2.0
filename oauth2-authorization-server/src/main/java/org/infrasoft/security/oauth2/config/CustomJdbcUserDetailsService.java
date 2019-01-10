package org.infrasoft.security.oauth2.config;

/**
 * Created by Rachit Bedi
 * Version 1.0
 */

import org.infrasoft.security.oauth2.model.Credentials;
import org.infrasoft.security.oauth2.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class CustomJdbcUserDetailsService implements UserDetailsService {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials credentials = credentialsRepository.findByUsername(username);

        if (credentials == null) {
            throw new UsernameNotFoundException(String.format("User %s cannot be found", username));
        }

        User user = new User(credentials.getUsername(),
                credentials.getPassword(),
                credentials.isEnabled(),
                true,
                true,
                true,
                credentials.getAuthorities());

        return user;
    }
}
