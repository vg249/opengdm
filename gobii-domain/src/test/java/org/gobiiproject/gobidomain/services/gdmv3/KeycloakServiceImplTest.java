package org.gobiiproject.gobidomain.services.gdmv3;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.gobiiproject.gobiimodel.config.KeycloakConfig;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(PowerMockRunner.class)
@WebAppConfiguration
public class KeycloakServiceImplTest {

    @Mock
    KeycloakConfig keycloakConfig;

    @InjectMocks
    KeycloakServiceImpl keycloakServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUser() throws Exception {
        // mock the builder
        KeycloakServiceImpl implSpy = spy(keycloakServiceImpl);


        //mock realmres
        Keycloak mockKeycloak = mock(Keycloak.class);
        RealmResource mockRealm = mock(RealmResource.class);
        UsersResource mockUsers = mock(UsersResource.class);
        UserResource mockUser = mock(UserResource.class);
        UserRepresentation mockRep  = new UserRepresentation();
        mockRep.setCreatedTimestamp(12345L);


        when(keycloakConfig.getRealm()).thenReturn("test-realm");
        when(mockKeycloak.realm("test-realm")).thenReturn(mockRealm);
        when(mockRealm.users()).thenReturn(mockUsers);
        when(mockUsers.get("some-uuid-id")).thenReturn(mockUser);
        when(mockUser.toRepresentation()).thenReturn(mockRep);

        doReturn(mockKeycloak).when(implSpy).getKeycloakAdminClient();

        ContactDTO dto = implSpy.getUser("some-uuid-id");
        assertNotNull(dto);
        //mock 

    }
}