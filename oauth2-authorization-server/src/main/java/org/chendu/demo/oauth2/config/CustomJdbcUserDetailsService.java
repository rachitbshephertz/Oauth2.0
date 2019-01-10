package org.chendu.demo.oauth2.config;

import org.chendu.demo.oauth2.repository.CredentialsRepository;
import org.chendu.demo.oauth2.model.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Chen Du @10/31/2018.
 * Version 1.0
 */
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
