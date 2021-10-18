package org.gobiiproject.gobiiclient.gobii;

import java.util.List;

import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.junit.Ignore;

public class GobiiClientContextTest {
    
    

    @Ignore
    public void testGetInstance() throws Exception {
        GobiiClientContext gobiiClientContext = 
            GobiiClientContext.getInstance(
                "http://localhost:8282/gdm/crops/dev/", true);
        //GobiiClientContext context = 
        //    GobiiClientContext
        //        .getInstance("https://gdm-extractor.irri.org/gobii-rice/", true); 
        List<String> crops = gobiiClientContext.getCropTypeTypes();
    }
}
