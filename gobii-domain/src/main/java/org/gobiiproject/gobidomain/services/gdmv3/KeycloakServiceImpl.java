package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.gobiiproject.gobidomain.services.gdmv3.exceptions.EntityDoesNotExistException;
import org.gobiiproject.gobiimodel.config.KeycloakConfig;
import org.gobiiproject.gobiimodel.config.Roles;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

    @Autowired
    KeycloakConfig keycloakConfig;
    
    @Override
    public ContactDTO getUser(String uuid) throws Exception {
        Keycloak keycloak = this.getKeycloakAdminClient();

        UserRepresentation user = keycloak.realm(keycloakConfig.getRealm()).users().get(uuid).toRepresentation();
        keycloak.close();

        ContactDTO contact = Optional.ofNullable(user)
                             .map(u -> this.getContactDTO(u))
                             .orElseThrow(()-> new EntityDoesNotExistException("Contact"));

        
        return contact;
    }

    public List<ContactDTO> getKeycloakUsers(String cropType, String role, Integer page, Integer pageSize) throws Exception {
        Keycloak keycloak = this.getKeycloakAdminClient();
        
        String rolePath = Optional.ofNullable(role)
                                   .map(r -> (!role.equals(Roles.USER)) ?  String.format("/%s/%s", cropType, role) 
                                                                        : String.format("/%s", cropType)
                                    )
                                   .orElseGet( () -> String.format("/%s", cropType));
        
        List<GroupRepresentation> groups =
            keycloak.realm(keycloakConfig.getRealm())
                    .groups()
                    .groups(cropType, 0, 1);


        String groupId = digList(groups, rolePath);
        if (groupId == null) return new ArrayList<ContactDTO>();
        
        //get the members
        List<UserRepresentation> usersInRole = 
            keycloak.realm(keycloakConfig.getRealm()).groups().group(groupId).members(page, pageSize);

        List<ContactDTO> contactDTOs = new ArrayList<>();
        
        usersInRole.forEach(member -> {
            ContactDTO contactDTO = this.getContactDTO(member);
            contactDTOs.add(contactDTO);
        });
        keycloak.close();

        return contactDTOs;       
    }

    @lombok.Generated //exclude from code coverage 
    public Keycloak getKeycloakAdminClient() {
        log.debug("KC Admin-CLI Username: " +  keycloakConfig.getAdminUsername() );
        Keycloak keycloak = KeycloakBuilder.builder()
                            .serverUrl(keycloakConfig.getAuthServerUrl())
                            .realm("master")
                            .username(keycloakConfig.getAdminUsername())
                            .password(keycloakConfig.getAdminPassword())
                            .grantType("password")
                            .clientId("admin-cli")
                            .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                            .build();
        keycloak.tokenManager().getAccessToken();
        return keycloak;
    }

    
    private String digList(List<GroupRepresentation> groups, String path) {
        if (groups == null || groups.size() == 0 ) return null;
        String groupId = null;
        String pathL = path.toLowerCase();
        for (int i = 0; i < groups.size(); i++) {
            GroupRepresentation current = groups.get(i);
            //found
            if (current.getPath().toLowerCase().equals(pathL)) return current.getId();

            //check subgroups
            groupId = digList(current.getSubGroups(), path);
            if (groupId != null) return groupId;
        }
        return groupId;
    }
    
    private ContactDTO getContactDTO(UserRepresentation user) {
        ContactDTO contactDTO =  new ContactDTO();
        contactDTO.setPiContactFirstName(user.getFirstName());
        contactDTO.setPiContactLastName(user.getLastName());
        contactDTO.setPiContactId(user.getId());
        contactDTO.setUsername(user.getUsername());
        contactDTO.setCreatedDate(new java.util.Date(user.getCreatedTimestamp()));
        contactDTO.setEmail(user.getEmail());
        //check for organization
        Map<String, List<String>> attributes = user.getAttributes();
        String organization = 
                                Optional.ofNullable( attributes )
                                      .map(attrs -> attrs.get("organization"))
                                      .map(orgList -> orgList.get(0))
                                      .orElse(null);
        contactDTO.setOrganizationName(organization);
        return contactDTO;

    }
}