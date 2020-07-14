package org.gobiiproject.gobiiweb.security.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.gobiiproject.gobiiweb.automation.ResponseUtils;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * This interceptor determines if the user is an ADMIN OR has general permission to access the crop app
 * The user must have /{cropType} group at least.
 */
public class CropUserFilter extends GenericFilterBean {

    @Override
    @SuppressWarnings("all")
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
         
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Optional.ofNullable(authentication)
                    .map(a -> a.getPrincipal())
                    .orElse(null) instanceof KeycloakPrincipal
            ) {

            String currentCropType = null;
            try {
                currentCropType = CropRequestAnalyzer.getGobiiCropType((HttpServletRequest) request).toLowerCase();
            } catch (Exception e) {
                throw new ServletException(e);
            }
            KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();
            AccessToken token = kp.getKeycloakSecurityContext().getToken();
            //Get the major roles
            //and append
            Map<String, Object> otherClaims = token.getOtherClaims(); 

            //bypass if admin
            List<String> roles = (List<String>) Optional.ofNullable(otherClaims.get("roles")).orElse(new ArrayList<>());
            if (roles.contains("ADMIN"))  {
                chain.doFilter(request, response); //continue on
                return;
            }

            List<String> userGroups = (List<String>) Optional.ofNullable(otherClaims.get("groups")).orElse(new ArrayList<>());
            Iterator<String> iter = userGroups.iterator();
            while (iter.hasNext()) {
                if (iter.next()
                        .toString()
                        .startsWith(
                            String.format("/%s", currentCropType)
                        )
                    ) {
                    chain.doFilter(request, response);
                    return;
                }
               
            }
            ResponseUtils.sendUnauthorizedResponse(response);
            return;
        } 

        chain.doFilter(request, response);

    }

    
    
}