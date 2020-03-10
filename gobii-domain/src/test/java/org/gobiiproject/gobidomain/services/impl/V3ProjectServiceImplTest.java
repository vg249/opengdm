package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapV3Project;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class V3ProjectServiceImplTest {

    @Mock
    private DtoMapV3Project dtoMapV3Project;

    @InjectMocks
    private V3ProjectServiceImpl v3ProjectServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSimple() {
        assert dtoMapV3Project != null ;
    }


}