package org.infrasoft.security.oauth2.repository;

import org.infrasoft.security.oauth2.model.ClientIdServiceProviderMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rachit Bedi @27/12/2018.
 * Version 1.0
 */
@Repository
public interface ClientIdServiceProviderMappingRepository extends JpaRepository<ClientIdServiceProviderMapping, Long> {

	ClientIdServiceProviderMapping findByserviceProviderId(String serviceProviderId);
	
	long countByserviceProviderId(String serviceProviderId);

    List<ClientIdServiceProviderMapping> findAll();
}
