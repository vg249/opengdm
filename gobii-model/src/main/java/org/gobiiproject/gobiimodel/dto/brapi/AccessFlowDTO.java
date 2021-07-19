package org.gobiiproject.gobiimodel.dto.brapi;

import lombok.Data;

@Data
public class AccessFlowDTO {
    
    private String type;

    private String authUrl;

    private String clientId;

    private String keycloakRealm;
    
}
