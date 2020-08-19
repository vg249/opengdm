package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobidomain.services.brapi.CallSetServiceImpl;
import org.gobiiproject.gobidomain.services.brapi.MockSetup;
import org.gobiiproject.gobiisampletrackingdao.DnaRunDaoImpl;
import org.gobiiproject.gobiisampletrackingdao.ProtocolDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class ProtocolServiceImplTest {

    @InjectMocks
    private ProtocolServiceImpl protocolService;

    @Mock
    private ProtocolDao protocolDao;

    MockSetup mockSetup;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockSetup  = new MockSetup();
    }

    @Test
    public void getProtocolByIdTest() {
        int testPageSize = 10;
        mockSetup.createMockProtocols(testPageSize);

    }
}
