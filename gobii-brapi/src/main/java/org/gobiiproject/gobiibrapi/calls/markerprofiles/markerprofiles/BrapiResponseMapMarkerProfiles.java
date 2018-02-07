package org.gobiiproject.gobiibrapi.calls.markerprofiles.markerprofiles;

import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobidomain.services.SampleService;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExperimentDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.SampleDTO;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapMarkerProfiles {

    @Autowired
    private SampleService sampleService;

    public BrapiResponseMarkerProfilesMaster getBrapiResponseMarkerProfilesByExternalId(String externalCode) {

        BrapiResponseMarkerProfilesMaster returnVal = new BrapiResponseMarkerProfilesMaster();
        SampleDTO sampleDTO = sampleService.getSampleDetailsByExternalCode(externalCode);

        if( sampleDTO.getGermplasmId() > 0 )  {

            returnVal.setAnalysisMethod(sampleDTO.getAnalysisMethodName());
            returnVal.setExtractDbId(sampleDTO.getDnaSampleId().toString());
            returnVal.setGermplasmDbId(sampleDTO.getGermplasmId().toString());
            returnVal.setUniqueDisplayName(sampleDTO.getGermplasmName());
            returnVal.setMarkerprofileDbId(sampleDTO.getExternalCode());
        }

        returnVal.setData(new ArrayList<>());
        return returnVal;

    }

}
