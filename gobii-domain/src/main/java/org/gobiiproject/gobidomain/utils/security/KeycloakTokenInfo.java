package org.gobiiproject.gobidomain.utils.security;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class KeycloakTokenInfo {

    static Logger LOGGER = LoggerFactory.getLogger(KeycloakTokenInfo.class);

    public static AccessToken getAccessToken() {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication.getPrincipal() instanceof KeycloakPrincipal) {
                KeycloakPrincipal<org.keycloak.KeycloakSecurityContext> kp =
                    (KeycloakPrincipal<org.keycloak.KeycloakSecurityContext>)
                        authentication.getPrincipal();
                AccessToken token = kp.getKeycloakSecurityContext().getToken();
                return token;
            }
            else {
                throw new GobiiDomainException("Unable to fetch authorization information");
            }


        }
        catch (Exception e) {
            LOGGER.error("Unable to fetch authorization information.", e);
            throw new GobiiDomainException(e);

        }
    }

    public static List<String> getUserGroups() {
        try {
            Map<String, Object> otherClaims = getAccessToken().getOtherClaims();

            List<String> userGroups = (List<String>) Optional.ofNullable(otherClaims.get("groups"))
                .orElse(new ArrayList<>());
            return userGroups;
        }
        catch (Exception e) {
            LOGGER.error("Unable to fetch authorization information.", e);
            throw new GobiiDomainException(e);
        }
    }
}
