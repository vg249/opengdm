package org.gobiiproject.gobiiclient.dtorequests.brapi;


import org.apache.commons.lang.ObjectUtils;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiibrapi.calls.germplasm.BrapiResponseGermplasmSearch;
import org.gobiiproject.gobiibrapi.calls.studies.observationvariables.BrapiResponseObservationVariablesDetail;
import org.gobiiproject.gobiibrapi.calls.studies.observationvariables.BrapiResponseObservationVariablesMaster;
import org.gobiiproject.gobiibrapi.core.derived.BrapiResponseEnvelopeMaster;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.BrapiResourceDerived;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiTestObservationVariables {


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void getObservationVariables() throws Exception {


        RestUri restUriObservationVariables = ClientContext.getInstance(null, false)
                .getUriFactory(ControllerType.BRAPI)
                .childResourceByUriIdParam(ServiceRequestId.URL_STUDIES,
                        ServiceRequestId.URL_OBSERVATION_VARIABLES);
        restUriObservationVariables.setParamValue("id", "1");

        BrapiResourceDerived<ObjectUtils.Null, BrapiResponseObservationVariablesMaster, BrapiResponseObservationVariablesDetail> brapiResourceDerived =
                new BrapiResourceDerived<>(restUriObservationVariables,
                        ObjectUtils.Null.class,
                        BrapiResponseObservationVariablesMaster.class,
                        BrapiResponseObservationVariablesDetail.class);

        BrapiResponseEnvelopeMaster<BrapiResponseObservationVariablesMaster> brapiResponseEnvelopeMaster =
                brapiResourceDerived.getFromMasterResource();

        BrapiTestResponseStructure.validatateBrapiResponseStructure(brapiResponseEnvelopeMaster.getBrapiMetaData());

        BrapiResponseObservationVariablesMaster brapiResponseObservationVariablesMaster = brapiResponseEnvelopeMaster.getResult();


        Assert.assertNotNull(brapiResponseObservationVariablesMaster.getStudyDbId());
        Assert.assertNotNull(brapiResponseObservationVariablesMaster.getTrialName());
        Assert.assertTrue(brapiResponseObservationVariablesMaster.getData().size() > 0);
        Assert.assertNotNull(brapiResponseObservationVariablesMaster.getData().get(0).getName());

    }
}
