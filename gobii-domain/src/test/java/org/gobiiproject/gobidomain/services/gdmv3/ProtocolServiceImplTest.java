package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobidomain.services.brapi.CallSetServiceImpl;
import org.gobiiproject.gobidomain.services.brapi.MockSetup;
import org.gobiiproject.gobiimodel.dto.gdmv3.ProtocolDTO;
import org.gobiiproject.gobiimodel.entity.Protocol;
import org.gobiiproject.gobiisampletrackingdao.DnaRunDaoImpl;
import org.gobiiproject.gobiisampletrackingdao.ProtocolDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Random;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@WebAppConfiguration
public class ProtocolServiceImplTest {

    @InjectMocks
    private ProtocolServiceImpl protocolService;

    @Mock
    private ProtocolDao protocolDao;

    MockSetup mockSetup;

    Random random = new Random();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockSetup  = new MockSetup();
    }

    @Test
    public void getProtocolByIdTest() {
        int testPageSize = 10;
        mockSetup.createMockProtocols(testPageSize);

        Protocol testProtocol = mockSetup.mockProtocols
            .get(random.nextInt(mockSetup.mockProtocols.size() - 1));

        when(protocolDao.getProtocolById(testProtocol.getProtocolId())).thenReturn(testProtocol);

        ProtocolDTO protocolDTO = protocolService.getProtocolById(testProtocol.getProtocolId());

        assertTrue("protocolId mapping failed",
            protocolDTO.getProtocolId() == testProtocol.getProtocolId());

        assertTrue("protocolName mapping failed",
            protocolDTO.getProtocolName().equals(testProtocol.getName()));

        assertTrue("protocolDescription mapping failed",
            protocolDTO.getProtocolDescription().equals(testProtocol.getDescription()));

        assertTrue("platformId mapping failed",
            protocolDTO.getPlatformId() == testProtocol.getPlatform().getPlatformId());


    }
}
