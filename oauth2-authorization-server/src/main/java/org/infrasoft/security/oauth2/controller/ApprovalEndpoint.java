package org.infrasoft.security.oauth2.controller;

import org.infrasoft.security.oauth2.repository.ClientIdServiceProviderMappingRepository;
import org.infrasoft.security.oauth2.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Controller for displaying the approval page for the authorization server.
 * 
 * 
 */
@FrameworkEndpoint
@SessionAttributes("authorizationRequest")
public class ApprovalEndpoint {
	
	@Autowired
	private ApprovalService approvalService;
	
	@RequestMapping("/oauth/custom_confirm_access")
	public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
		System.out.println("IN AUTHORIZE ENDPOINT");
		
		final String approvalContent = createTemplate(model, request);
		System.out.println(approvalContent);
		if (request.getAttribute("_csrf") != null) {
			model.put("_csrf", request.getAttribute("_csrf"));
		}
		View approvalView = new View() {
			@Override
			public String getContentType() {
				return "text/html";
			}

			@Override
			public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
				response.setContentType(getContentType());
				response.getWriter().append(approvalContent);
			}
		};
		return new ModelAndView(approvalView, model);
	}

	protected String createTemplate(Map<String, Object> model, HttpServletRequest request) {
		AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
		String clientId = authorizationRequest.getClientId();

		String ServiceProviderName = approvalService.getServiceProviderName(clientId);

		StringBuilder builder = new StringBuilder();
		builder.append("<html lang='en'><head><title>Login V19</title><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1'>"
				+ "<link rel='icon' type='image/png' href='resources/images/icons/favicon.ico'/>"
				+ "<link rel='stylesheet' type='text/css' href='resources/vendor/bootstrap/css/bootstrap.min.css'>"
				+ "<link rel='stylesheet' type='text/css' href='resources/fonts/font-awesome-4.7.0/css/font-awesome.min.css'>"
				+ "<link rel='stylesheet' type='text/css' href='resources/fonts/Linearicons-Free-v1.0.0/icon-font.min.css'>"
				+ "<link rel='stylesheet' type='text/css' href='resources/vendor/animate/animate.css'>"
				+ "<link rel='stylesheet' type='text/css' href='resources/vendor/css-hamburgers/hamburgers.min.css'>"
				+ "<link rel='stylesheet' type='text/css' href='resources/vendor/animsition/css/animsition.min.css'>"
				+ "<link rel='stylesheet' type='text/css' href='resources/vendor/select2/select2.min.css'>"
				+ "<link rel='stylesheet' type='text/css' href='resources/vendor/daterangepicker/daterangepicker.css'>"
				+ "<link rel='stylesheet' type='text/css' href='resources/css/util.css'>"
				+ "<link rel='stylesheet' type='text/css' href='resources/css/main.css'>"
				+ " <style> .form-actions {margin: 0; background-color: transparent; text-align: center; } td { padding:0 5px 0 5px; } #over img { margin-top: 60px; margin-left: auto; margin-right: auto; display: block; } </style>"
				+ " </head>"
				+ "<body><div class='limiter'><div id='over' style='width:100%;'><img src= 'resources/images/InfraBank_Logo.png' alt='infrabank' align='top'></div><div class='wrap-login100 p-l-55 p-r-55 p-t-65 p-b-50'><div class='container-login100'>"
				+ "<center><h1><b>OAuth Approval</b></h1>");
		builder.append("<center><p><b>Do you authorize \"").append(HtmlUtils.htmlEscape(ServiceProviderName));
		builder.append("\" to access your protected resources?</b></p></center><hr size=2>");
		builder.append("<form class='login100-form validate-form' id=\"confirmationForm\" name=\"confirmationForm\" action=\"");

		String requestPath = ServletUriComponentsBuilder.fromContextPath(request).build().getPath();
		if (requestPath == null) {
			requestPath = "";
		}

		builder.append(requestPath).append("/oauth/authorize\" method=\"post\">");
		builder.append("<input name=\"user_oauth_approval\" value=\"true\" type=\"hidden\"/>");

		String csrfTemplate = null;
		CsrfToken csrfToken = (CsrfToken) (model.containsKey("_csrf") ? model.get("_csrf") : request.getAttribute("_csrf"));
		if (csrfToken != null) {
			csrfTemplate = "<input type=\"hidden\" name=\"" + HtmlUtils.htmlEscape(csrfToken.getParameterName()) +
					"\" value=\"" + HtmlUtils.htmlEscape(csrfToken.getToken()) + "\" />";
		}
		if (csrfTemplate != null) {
			builder.append(csrfTemplate);
		}

		String authorizeInputTemplate = "<div class='form-actions'><centre><input name=\"authorize\" value=\"Authorize\" type=\"submit\" class='btn btn-primary center-block' /></centre></div></form>";

		if (model.containsKey("scopes") || request.getAttribute("scopes") != null) {
			builder.append(createScopes(model, request));
			builder.append(authorizeInputTemplate);
		} else {
			builder.append(authorizeInputTemplate);
			builder.append("<form id=\"denialForm\" name=\"denialForm\" action=\"");
			builder.append(requestPath).append("/oauth/authorize\" method=\"post\">");
			builder.append("<input name=\"user_oauth_approval\" value=\"false\" type=\"hidden\"/>");
			if (csrfTemplate != null) {
				builder.append(csrfTemplate);
			}
			builder.append("<label><input name=\"deny\" value=\"Deny\" type=\"submit\"/></label></form>");
		}

		builder.append("</div></div></div></body></html><script src='resources/js/main.js'></script>");

		return builder.toString();
	}

	private CharSequence createScopes(Map<String, Object> model, HttpServletRequest request) {
		StringBuilder builder = new StringBuilder("<table align='center'>");
		@SuppressWarnings("unchecked")
		Map<String, String> scopes = (Map<String, String>) (model.containsKey("scopes") ?
				model.get("scopes") : request.getAttribute("scopes"));
		for (String scope : scopes.keySet()) {
			String approved = "true".equals(scopes.get(scope)) ? " checked" : "";
			String denied = !"true".equals(scopes.get(scope)) ? " checked" : "";
			scope = HtmlUtils.htmlEscape(scope);

			builder.append("<center><tr><div class=\"form-group\">");
			builder.append(scope).append(": <input type=\"radio\" name=\"");
			builder.append(scope).append("\" value=\"true\"").append(approved).append(">Approve</input> ");
			builder.append("<input type=\"radio\" name=\"").append(scope).append("\" value=\"false\"");
			builder.append(denied).append(">Deny</input></div></tr></center>");
		}
		builder.append("</table><br/>");
		return builder.toString();
	}

}
