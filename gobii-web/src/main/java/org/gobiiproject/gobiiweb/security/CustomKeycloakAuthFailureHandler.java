package org.gobiiproject.gobiiweb.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.gobiiproject.gobiiapimodel.payload.sampletracking.ErrorPayload;
import org.keycloak.adapters.springsecurity.authentication.KeycloakCookieBasedRedirect;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomKeycloakAuthFailureHandler implements AuthenticationFailureHandler {
    private static String UNAUTHORIZED_PAYLOAD;

    static {
        ErrorPayload payload = new ErrorPayload();
        payload.setError("Unauthorized");
        ObjectMapper mapper = new ObjectMapper();
        try {
            UNAUTHORIZED_PAYLOAD = mapper.writeValueAsString(payload);
        } catch (JsonProcessingException jpe) {
            UNAUTHORIZED_PAYLOAD = "Unauthorized";
        }
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        if (!response.isCommitted()) {
            if (KeycloakCookieBasedRedirect.getRedirectUrlFromCookie(request) != null) {
                response.addCookie(KeycloakCookieBasedRedirect.createCookieFromRedirectUrl(null));
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON);
            PrintWriter out = response.getWriter();
            out.println(UNAUTHORIZED_PAYLOAD);
        } else {
            if (200 <= response.getStatus() && response.getStatus() < 300) {
                throw new RuntimeException("Success response was committed while authentication failed!", exception);
            }
        }

    }
    
}