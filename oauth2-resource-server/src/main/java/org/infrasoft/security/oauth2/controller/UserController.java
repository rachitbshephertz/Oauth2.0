package org.infrasoft.security.oauth2.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Controller
@RequestMapping("user")
public class UserController {
	
    @Autowired
    private ApprovalStore approvalStore;
    
    @Autowired
    private TokenStore tokenStore;
    
    @RequestMapping(value="/approval/revoke",method= RequestMethod.POST)
    public ResponseEntity<String> revokeApproval(@ModelAttribute Approval approval) {
    	
    	JSONObject responseJSON = new JSONObject();
    	
		try {
			responseJSON.put("success", true);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String responseJSONString = null;

		approvalStore.revokeApprovals(approvalStore.getApprovals( approval.getUserId(), approval.getClientId()));		
		Collection<OAuth2AccessToken> accesstokens = tokenStore.findTokensByClientIdAndUserName(approval.getClientId(), approval.getUserId());
		Iterator iterator = accesstokens.iterator();
		while (iterator.hasNext()) {
		OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(iterator.next().toString());
        if (oAuth2AccessToken != null) {
            OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
            if (oAuth2RefreshToken != null)
                tokenStore.removeRefreshToken(oAuth2RefreshToken);

            tokenStore.removeAccessToken(oAuth2AccessToken);
        }	
		}
        
//        tokenStore.findTokensByClientIdAndUserName(approval.getClientId(), approval.getUserId())
//                .forEach(tokenStore::removeAccessToken);
        
        responseJSONString = responseJSON.toString();
        return new ResponseEntity<>(responseJSONString, HttpStatus.OK);
    }

}
