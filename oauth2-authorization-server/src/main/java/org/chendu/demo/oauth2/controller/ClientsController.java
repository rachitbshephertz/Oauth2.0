package org.chendu.demo.oauth2.controller;

import org.chendu.demo.oauth2.config.EditorAuthorityProperty;
import org.chendu.demo.oauth2.config.EditorSplitCollection;
import org.chendu.demo.oauth2.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Chen Du @10/30/2018.
 * Version 1.0
 */
@Controller
@RequestMapping("clients")
public class ClientsController {

    @Autowired
    private JdbcClientDetailsService clientDetailsService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Collection.class, new EditorSplitCollection(Set.class, ","));
        binder.registerCustomEditor(GrantedAuthority.class, new EditorAuthorityProperty());
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_OAUTH_ADMIN')")
    public String showEditForm(@RequestParam(value = "client", required = false) String clientId, Model model) {
        ClientDetails clientDetails;
        if (null == clientId) {
            clientDetails = new BaseClientDetails();
        } else {
            clientDetails = clientDetailsService.loadClientByClientId(clientId);
        }
        model.addAttribute("clientDetails", clientDetails);
        return "form";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_OAUTH_ADMIN')")
    public String editClient(
            @ModelAttribute BaseClientDetails clientDetails,
            @RequestParam(value = "newClient", required = false) String newClient) {
        clientDetails.setClientSecret(Utils.passwordEncoder(clientDetails.getClientSecret()));
        if (newClient == null) {
            clientDetailsService.updateClientDetails(clientDetails);
        } else {
            clientDetailsService.addClientDetails(clientDetails);
        }
        if (!clientDetails.getClientSecret().isEmpty()) {
            clientDetailsService.updateClientSecret(clientDetails.getClientId(), clientDetails.getClientSecret());
        }
        return "redirect:/";
    }

    @RequestMapping(value = "{client.clientId}/delete", method = RequestMethod.POST)
    public String deleteClient(@ModelAttribute BaseClientDetails ClientDetails, @PathVariable("client.clientId") String id) {
        clientDetailsService.removeClientDetails(clientDetailsService.loadClientByClientId(id).toString());
        return "redirect:/";
    }
}
