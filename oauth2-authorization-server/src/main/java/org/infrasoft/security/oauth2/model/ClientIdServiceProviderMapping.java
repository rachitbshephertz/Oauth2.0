package org.infrasoft.security.oauth2.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Created by Rachit Bedi @27/12/2018.
 * Version 1.0
 */


@Entity
@Table(name = "client_id_service_provider_mapping")
public class ClientIdServiceProviderMapping implements Serializable {

    private static final long serialVersionUID = 3692881208438531637L;
    
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String clientId;

    @NotEmpty
    private String serviceProviderId;

    public String getClientId() {
		return clientId;
	}

    public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(String serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

}

