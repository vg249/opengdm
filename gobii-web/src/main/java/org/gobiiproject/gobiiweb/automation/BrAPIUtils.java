package org.gobiiproject.gobiiweb.automation;

import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterPayload;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerBrapiDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VCalaminos on 7/8/2019.
 */
public class BrAPIUtils {

    public static BrApiMasterPayload getListResponse (List dtoList) {

        Map<String, List> dtoMap = new HashMap<>();
        dtoMap.put("data", dtoList);

        BrApiMasterPayload<Map> payload = new BrApiMasterPayload<>(dtoMap);

        return payload;
    }

}
