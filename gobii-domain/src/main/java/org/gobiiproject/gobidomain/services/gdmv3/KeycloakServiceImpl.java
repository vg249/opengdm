package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.gobiiproject.gobidomain.services.gdmv3.exceptions.EntityDoesNotExistException;
import org.gobiiproject.gobiimodel.config.KeycloakConfig;
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
                                   .map(r -> String.format("/%s/%s", cropType, role.toLowerCase()))
                                   .orElse( String.format("/%s", cropType));
        
        if (rolePath.equals(String.format("/%s/user", cropType))) rolePath = String.format("/%s", cropType);

        List<GroupRepresentation> groups =
            keycloak.realm(keycloakConfig.getRealm())
                    .groups()
                    .groups(cropType, 0, 1);

        if (groups.size() == 0 ) return new ArrayList<ContactDTO>();

        GroupRepresentation group = groups.get(0);
        String groupId = null;

        if (group.getPath().equals(rolePath)) {
            groupId = group.getId();
        } else {
            //get subgroups
            List<GroupRepresentation> subgroup = group.getSubGroups();
            for (int i = 0; i< subgroup.size(); i++) {
                if (subgroup.get(i).getPath().equals(rolePath)) {
                    groupId = subgroup.get(i).getId();
                }
            }
        }

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

    private Keycloak getKeycloakAdminClient() {
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

    
    private ContactDTO getContactDTO(UserRepresentation user) {
        ContactDTO contactDTO =  new ContactDTO();
        contactDTO.setPiContactFirstName(user.getFirstName());
        contactDTO.setPiContactLastName(user.getLastName());
        contactDTO.setPiContactId(user.getId());
        contactDTO.setUsername(user.getUsername());
        contactDTO.setCreatedDate(new java.util.Date(user.getCreatedTimestamp()));

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