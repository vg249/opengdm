package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.List;

import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.security.TokenInfo;


public interface KeycloakService {
    public ContactDTO getUser(String uuid) throws Exception;

    public List<ContactDTO> getKeycloakUsers(String cropType, String role, Integer page, Integer pageSize) throws Exception;

	public TokenInfo getToken(String username, String password) throws Exception;
}