package org.gobiiproject.gobiiclient.gobii;

import java.util.List;

import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.junit.Test;

public class GobiiClientContextTest {

    @Test
    public void testGetInstance() throws Exception {
        GobiiClientContext context = 
            GobiiClientContext
                .getInstance("https://gdm-extractor.irri.org/gobii-rice/", true); 
        List<String> crops = context.getCropTypeTypes();

    }
}
