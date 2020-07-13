package org.gobiiproject.gobiiweb.filters;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

import org.gobiiproject.gobiiapimodel.payload.sampletracking.ErrorPayload;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.gobiiproject.gobiiweb.security.CropAuth;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.emory.mathcs.backport.java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CropAuthInterceptor extends HandlerInterceptorAdapter {

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
    @SuppressWarnings("all")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String currentCropType = CropRequestAnalyzer.getGobiiCropType(request).toLowerCase();
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.hasMethodAnnotation(CropAuth.class)) {
            CropAuth annotation = handlerMethod.getMethodAnnotation(CropAuth.class);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth.getPrincipal() instanceof KeycloakPrincipal) {
                KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) auth.getPrincipal();
                AccessToken token = kp.getKeycloakSecurityContext().getToken();
                //Get the major roles
                //and append
                Map<String, Object> otherClaims = token.getOtherClaims(); 

                //bypass if admin
                List<String> roles = (List<String>) Optional.ofNullable(otherClaims.get("roles")).orElse(new ArrayList<>());
                if (roles.contains("ADMIN"))  {
                    return true;
                }

                List<String> userGroups = (List<String>) Optional.ofNullable(otherClaims.get("groups")).orElse(new ArrayList<>());
                HashSet<String> currentGroups = new HashSet<>();
                currentGroups.addAll(userGroups);
                
                HashSet<String> requiredGroups = new HashSet<>();
                Arrays.asList(annotation.value()).forEach(role -> {
                    requiredGroups.add( String.format("/%s/%s", currentCropType, role.toString().toLowerCase()));
                });
                
                Set<String> validRoles = Sets.intersection(currentGroups, requiredGroups);

                if (Optional.ofNullable(validRoles).map(v ->  v.size()).orElse(0) > 0) {
                    return true;
                }    
            }
            //throw unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON);
            PrintWriter out = response.getWriter();
            out.println(UNAUTHORIZED_PAYLOAD);
            return false;

        }
        return super.preHandle(request, response, handler);
    }

    
    
}