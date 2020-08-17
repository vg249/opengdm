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
import javax.servlet.http.HttpServletResponse;

import org.gobiiproject.gobidomain.services.gdmv3.ContactService;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.gobiiproject.gobiiweb.automation.ResponseUtils;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import lombok.extern.slf4j.Slf4j;

/**
 * This filter determines if the user is an ADMIN OR has permission to access the crop endpoint
 * The user must have /{cropType} group at least OR has and ADMIN role. If the user is permitted 
 * to access the endpoint, the user will be added to the Contacts table if the user record does not
 * yet exist in the GDM database.
 */
@Slf4j
public class CropUserFilter extends GenericFilterBean {

    @Autowired
    ContactService contactService;

    @Override
    @SuppressWarnings("all")
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HttpServletRequest req = (HttpServletRequest) request;
        if (!this.shouldFilter(req.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        if (Optional.ofNullable(authentication)
                    .map(a -> a.getPrincipal())
                    .orElse(null) instanceof KeycloakPrincipal
            ) {
            
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            //Add the response headers
            String currentCropType = null;
            try {
                currentCropType = CropRequestAnalyzer.getGobiiCropType((HttpServletRequest) request).toLowerCase();
                httpResponse.setHeader(GobiiHttpHeaderNames.HEADER_NAME_GOBII_CROP, currentCropType);
                
            } catch (Exception e) {
                throw new ServletException(e);
            }
            KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();
            AccessToken token = kp.getKeycloakSecurityContext().getToken();
            //Get the major roles
            //and append
            Map<String, Object> otherClaims = token.getOtherClaims(); 
            //TODO:  including these in the response header seems to be a security risk.
            httpResponse.setHeader(GobiiHttpHeaderNames.HEADER_NAME_TOKEN, token.getAccessTokenHash());
            httpResponse.setHeader(GobiiHttpHeaderNames.HEADER_NAME_USERNAME, token.getPreferredUsername());
            //bypass if admin
            List<String> roles = (List<String>) Optional.ofNullable(otherClaims.get("roles")).orElse(new ArrayList<>());
            if (roles.contains("ADMIN"))  {
                try {
                    this.addToContacts(token);
                } catch (Exception e) {
                    //e.printStackTrace();
                    log.error("Could not add admin info to contacts table");
                }
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
                    //Check if the user is in the db, add if not found
                    try {
                        this.addToContacts(token);
                    } catch (Exception e) {
                        log.error("Could not add user info to contacts table");
                    }
                    chain.doFilter(request, response);
                    return;
                }
               
            }
            
            ResponseUtils.sendUnauthorizedResponse(response);
            return;
        } 

        chain.doFilter(request, response);

    }


    private void addToContacts(AccessToken token) throws Exception {
        String organization = Optional
                              .ofNullable(token.getOtherClaims().get("organization"))
                              .map(o -> o.toString())
                              .orElse(null);
                                            
        contactService.addContact(
            token.getPreferredUsername(),
            token.getGivenName(),
            token.getFamilyName(),
            token.getEmail(),
            organization,
            null
        );

    }

    private boolean shouldFilter(String path) {
        if (path.contains(GobiiControllerType.SERVICE_PATH_GOBII_V3)) return true;
        if (path.contains(GobiiControllerType.SERVICE_PATH_GOBII)) return true;
        return false;
    }
    
    
}