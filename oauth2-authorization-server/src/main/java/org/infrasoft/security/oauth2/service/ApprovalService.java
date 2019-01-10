package org.infrasoft.security.oauth2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.infrasoft.security.oauth2.repository.ClientIdServiceProviderMappingRepository;

@Service
public class ApprovalService {
	
	@Autowired
	private ClientIdServiceProviderMappingRepository clientIdServiceProviderMappingRepository;
	
	public String getServiceProviderName(String clientId) {
		
		String ServiceProviderName = null;
		ServiceProviderName = clientIdServiceProviderMappingRepository.getServiceProviderIdByclientId(clientId);
		if(ServiceProviderName.isEmpty())
			return "default";
		else
		return ServiceProviderName;
	}	

}
