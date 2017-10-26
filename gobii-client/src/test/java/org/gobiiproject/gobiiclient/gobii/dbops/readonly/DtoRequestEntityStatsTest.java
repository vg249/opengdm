package org.gobiiproject.gobiiclient.gobii.dbops.readonly;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestOrganizationTest;
import org.gobiiproject.gobiimodel.dto.entity.auditable.OrganizationDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.system.EntityStatsDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class DtoRequestEntityStatsTest {

    @BeforeClass
    public static void setUpClass() throws Exception {

        Assert.assertTrue(GobiiClientContextAuth.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }

    @Test
    public void testGetLastModified() throws Exception {


        //make a bunch of organiztion records
        List<Integer> organizationPkVals = (new GlobalPkColl<DtoCrudRequestOrganizationTest>()
                .getFreshPkVals(DtoCrudRequestOrganizationTest.class, GobiiEntityNameType.ORGANIZATION, 10));


        RestUri entityCountUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .entityCount(GobiiEntityNameType.ORGANIZATION);
        GobiiEnvelopeRestResource<EntityStatsDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(entityCountUri);

        PayloadEnvelope<EntityStatsDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(EntityStatsDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        Assert.assertTrue("No entity stats retrieved",
                (resultEnvelope.getPayload().getData().size() > 0));

        EntityStatsDTO entityStatsDTOBeforeAddMore = resultEnvelope.getPayload().getData().get(0);

        Date beforeAddTime = entityStatsDTOBeforeAddMore.getLastModified();

        //now do an update
        Thread.sleep(500);
        Integer arbitrarOrganizationId = organizationPkVals.get(0);
        RestUri restUriOrganizationForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_ORGANIZATION);
        restUriOrganizationForGetById.setParamValue("id", arbitrarOrganizationId.toString());
        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriOrganizationForGetById);
        PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(OrganizationDTO.class);

        String newName = UUID.randomUUID().toString();
        OrganizationDTO organizationDTO = resultEnvelopeForGetByID.getPayload().getData().get(0);
        organizationDTO.setName(newName);
        PayloadEnvelope<OrganizationDTO> organizationDTOResponseEnvelopeUpdate = gobiiEnvelopeRestResourceForGetById.put(OrganizationDTO.class,
                new PayloadEnvelope<>(organizationDTO, GobiiProcessType.UPDATE));
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(organizationDTOResponseEnvelopeUpdate.getHeader()));

        resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(OrganizationDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        OrganizationDTO organizationDTOReRetrieved = resultEnvelopeForGetByID.getPayload().getData().get(0);
        Assert.assertEquals(organizationDTOReRetrieved.getName(),newName);

        PayloadEnvelope<EntityStatsDTO> resultEnvelopePostUpdate = gobiiEnvelopeRestResource
                .get(EntityStatsDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopePostUpdate.getHeader()));
        EntityStatsDTO entityStatsDTOPosteUpdate = resultEnvelopePostUpdate.getPayload().getData().get(0);

        Assert.assertNotNull("The last modified date is null",
                entityStatsDTOPosteUpdate.getLastModified() );

        Assert.assertTrue("The new datestamp is not later than from before the update",
                entityStatsDTOPosteUpdate.getLastModified().compareTo(beforeAddTime) > 0 );

    }


}
