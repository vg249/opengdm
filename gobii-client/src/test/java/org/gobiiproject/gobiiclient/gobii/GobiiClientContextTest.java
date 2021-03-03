package org.gobiiproject.gobiiclient.gobii;

import static org.junit.Assert.assertTrue;

import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.junit.Test;

public class GobiiClientContextTest {
    
    

    @Test
    public void testGetInstance() throws Exception {
        GobiiClientContext gobiiClientContext = 
            GobiiClientContext.getInstance(
                "http://localhost:8282/gdm/crops/dev/", true);
        assertTrue("Crop Type", gobiiClientContext.getCropTypeTypes().size() > 0);
    }
}
