package org.infrasoft.security.oauth2.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.infrasoft.security.oauth2.config.EditorAuthorityProperty;
import org.infrasoft.security.oauth2.config.EditorSplitCollection;
import org.infrasoft.security.oauth2.model.ClientIdServiceProviderMapping;
import org.infrasoft.security.oauth2.repository.ClientIdServiceProviderMappingRepository;

import org.infrasoft.security.oauth2.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Controller
@RequestMapping("clients")
public class ClientsController {

	@Autowired
	private JdbcClientDetailsService clientDetailsService;

	@Autowired
	private ClientIdServiceProviderMappingRepository clientIdServiceProviderMappingRepository;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Collection.class, new EditorSplitCollection(Set.class, ","));
		binder.registerCustomEditor(GrantedAuthority.class, new EditorAuthorityProperty());
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@Secured("ROLE_ADMIN_OAUTH")
	public ResponseEntity<String> editClient(@ModelAttribute BaseClientDetails clientDetails,
			@RequestParam(value = "newClient", required = true) Boolean newClient,
			@RequestParam(value = "serviceProviderId", required = true) String serviceProviderId,
			@RequestParam(value = "redirectURI", required = true) String redirectURI) throws JSONException {
		String responseJSONString = null;
		JSONObject responseJSON = new JSONObject();
		responseJSON.put("success", false);
		if (!newClient) {
			if (clientIdServiceProviderMappingRepository.countByserviceProviderId(serviceProviderId) == 0) {
				responseJSON.put("message", "No client found.");
				responseJSONString = responseJSON.toString();
				return new ResponseEntity<>(responseJSONString, HttpStatus.BAD_REQUEST);
			}
			BaseClientDetails clientDetailsOriginal = new BaseClientDetails();
			clientDetailsOriginal = (BaseClientDetails) clientDetailsService
					.loadClientByClientId(clientDetails.getClientId());
			clientDetailsOriginal.setScope(clientDetails.getScope());
			clientDetailsService.updateClientDetails(clientDetailsOriginal);
			responseJSON.put("success", true);
		} else {
			if (clientIdServiceProviderMappingRepository.countByserviceProviderId(serviceProviderId) > 0) {
				responseJSON.put("message", "Client already registered.");
				responseJSONString = responseJSON.toString();
				return new ResponseEntity<>(responseJSONString, HttpStatus.ALREADY_REPORTED);
			}
			ClientIdServiceProviderMapping clientIdServiceProviderMapping = new ClientIdServiceProviderMapping();
			// AccessTokenValidity is 30 days
			clientDetails.setAccessTokenValiditySeconds(2592000);
			// RefreshTokenValidity is 5 years
			clientDetails.setRefreshTokenValiditySeconds(155520000);

			LinkedHashSet<String> resourceIds = new LinkedHashSet<String>();
			resourceIds.add("product_api");
			clientDetails.setResourceIds(resourceIds);

			LinkedHashSet<String> registeredRedirectUri = new LinkedHashSet<String>();
			registeredRedirectUri.add(redirectURI + "?service_provider=" + serviceProviderId);
			clientDetails.setRegisteredRedirectUri(registeredRedirectUri);

			LinkedHashSet<String> authorizedGrantTypes = new LinkedHashSet<String>();
			authorizedGrantTypes.add("authorization_code");
			authorizedGrantTypes.add("refresh_token");
			clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);

			LinkedHashSet<String> autoApproveScopes = new LinkedHashSet<String>();
			autoApproveScopes.add("false");
			clientDetails.setAutoApproveScopes(autoApproveScopes);

			String randomClientId = RandomStringUtils.random(20, true, true);
			clientDetails.setClientId(randomClientId);
			System.out.println(randomClientId);

			String randomClientSecret = RandomStringUtils.random(40, true, true);
			clientDetails.setClientSecret(randomClientSecret);
			System.out.println(randomClientSecret);

			clientDetails.setClientSecret(Utils.passwordEncoder(clientDetails.getClientSecret()));

			clientIdServiceProviderMapping.setClientId(randomClientId);
			clientIdServiceProviderMapping.setServiceProviderId(serviceProviderId);

			clientDetailsService.addClientDetails(clientDetails);
			clientIdServiceProviderMappingRepository.save(clientIdServiceProviderMapping);

			responseJSON.put("success", true);
			responseJSON.put("clientId", randomClientId);
			responseJSON.put("clientSecret", randomClientSecret);
			responseJSON.put("serviceProviderId", serviceProviderId);
			responseJSON.put("message", "Client registeration sucessful");
		}
		responseJSONString = responseJSON.toString();
		return new ResponseEntity<>(responseJSONString, HttpStatus.OK);
	}

	@RequestMapping(value = "/refreshClientSecret", method = RequestMethod.POST)
	@Secured("ROLE_ADMIN_OAUTH")
	public ResponseEntity<String> refreshClientSecret(
			@RequestParam(value = "serviceProviderId", required = true) String serviceProviderId,
			@RequestParam(value = "clientId", required = true) String clientId) throws JSONException {
		String responseJSONString = null;
		JSONObject responseJSON = new JSONObject();
		responseJSON.put("success", false);

		if (clientIdServiceProviderMappingRepository.countByserviceProviderId(serviceProviderId) == 0) {
			responseJSON.put("message", "No client found.");
			responseJSONString = responseJSON.toString();
			return new ResponseEntity<>(responseJSONString, HttpStatus.BAD_REQUEST);
		}
		
		String randomClientSecret = RandomStringUtils.random(40, true, true);
		clientDetailsService.updateClientSecret(clientId, Utils.passwordEncoder(randomClientSecret));
		
		responseJSON.put("success", true);
		responseJSON.put("clientSecret", randomClientSecret);

		responseJSONString = responseJSON.toString();
		return new ResponseEntity<>(responseJSONString, HttpStatus.OK);
	}

	@RequestMapping(value = "{client.clientId}/delete", method = RequestMethod.POST)
	public String deleteClient(@ModelAttribute BaseClientDetails ClientDetails,
			@PathVariable("client.clientId") String id) {
		clientDetailsService.removeClientDetails(clientDetailsService.loadClientByClientId(id).toString());
		return "redirect:/";
	}
}
