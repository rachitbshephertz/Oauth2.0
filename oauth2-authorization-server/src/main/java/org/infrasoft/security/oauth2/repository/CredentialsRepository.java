package org.infrasoft.security.oauth2.repository;

import org.infrasoft.security.oauth2.model.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Chen Du @10/31/2018.
 * Version 1.0
 */
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    Credentials findByUsername(String username);
}
