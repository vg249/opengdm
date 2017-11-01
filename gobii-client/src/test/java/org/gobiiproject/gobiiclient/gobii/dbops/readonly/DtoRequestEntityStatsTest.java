package org.gobiiproject.gobiiclient.gobii.dbops.readonly;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestContactTest;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestOrganizationTest;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestProjectTest;
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


        RestUri entityLastModifiedUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .entityLastModified(GobiiEntityNameType.ORGANIZATION);
        GobiiEnvelopeRestResource<EntityStatsDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(entityLastModifiedUri);

        PayloadEnvelope<EntityStatsDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(EntityStatsDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        Assert.assertTrue("No entity stats retrieved",
                (resultEnvelope.getPayload().getData().size() > 0));

        EntityStatsDTO entityStatsDTOBeforeAddMore = resultEnvelope.getPayload().getData().get(0);

        Date beforeAddTime = entityStatsDTOBeforeAddMore.getLastModified();
        Thread.sleep(500);

        // verify that the time is the same without having done an update
        resultEnvelope = gobiiEnvelopeRestResource
                .get(EntityStatsDTO.class);
        entityStatsDTOBeforeAddMore = resultEnvelope.getPayload().getData().get(0);

        Assert.assertEquals("The date time stamp should not have changed without doing an update",
                entityStatsDTOBeforeAddMore.getLastModified(), beforeAddTime);

        //verify that the time is the same when _another_ entity is modified
        List<Integer> contactPkVals = (new GlobalPkColl<DtoCrudRequestContactTest>()
                .getFreshPkVals(DtoCrudRequestContactTest.class, GobiiEntityNameType.CONTACT, 10));
        resultEnvelope = gobiiEnvelopeRestResource
                .get(EntityStatsDTO.class);
        entityStatsDTOBeforeAddMore = resultEnvelope.getPayload().getData().get(0);

        Assert.assertEquals("The date time stamp should not have changed when another entity was created",
                entityStatsDTOBeforeAddMore.getLastModified(), beforeAddTime);

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
        Assert.assertEquals(organizationDTOReRetrieved.getName(), newName);

        PayloadEnvelope<EntityStatsDTO> resultEnvelopePostUpdate = gobiiEnvelopeRestResource
                .get(EntityStatsDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopePostUpdate.getHeader()));
        EntityStatsDTO entityStatsDTOPosteUpdate = resultEnvelopePostUpdate.getPayload().getData().get(0);

        Assert.assertNotNull("The last modified date is null",
                entityStatsDTOPosteUpdate.getLastModified());

        Assert.assertTrue("The new datestamp is not later than from before the update",
                entityStatsDTOPosteUpdate.getLastModified().compareTo(beforeAddTime) > 0);

    }

    @Test
    public void testGetCount() throws Exception {

        RestUri entityCountdUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .entityCount(GobiiEntityNameType.PROJECT);
        GobiiEnvelopeRestResource<EntityStatsDTO> gobiiEnvelopeRestResource =
                new GobiiEnvelopeRestResource<>(entityCountdUri);

        PayloadEnvelope<EntityStatsDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(EntityStatsDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        EntityStatsDTO entityStatsDTOCount = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull("No entity count value was retrieved",
                entityStatsDTOCount.getCount());

        Integer projectCountBeforeInserts = entityStatsDTOCount.getCount();
        Integer numberOfProjectsToAdd = 5;

        // now create more projects
        (new GlobalPkColl<DtoCrudRequestProjectTest>())
                .getFreshPkVals(DtoCrudRequestProjectTest.class, GobiiEntityNameType.PROJECT, numberOfProjectsToAdd);

        PayloadEnvelope<EntityStatsDTO> resultEnvelopePostAdd = gobiiEnvelopeRestResource
                .get(EntityStatsDTO.class);
        EntityStatsDTO entityStatsDTOPostAdd = resultEnvelopePostAdd.getPayload().getData().get(0);

        Assert.assertTrue("The count retrieved was not increased by the number of records added",
                entityStatsDTOPostAdd.getCount() == (projectCountBeforeInserts + numberOfProjectsToAdd) );

        PayloadEnvelope<EntityStatsDTO> resultEnvelopePostAddRecheck = gobiiEnvelopeRestResource
                .get(EntityStatsDTO.class);
        EntityStatsDTO entityStatsDTOPostAddRecheck = resultEnvelopePostAddRecheck.getPayload().getData().get(0);

        Assert.assertTrue("The count did not remain the same when no records were added",
                entityStatsDTOPostAddRecheck.getCount() == (projectCountBeforeInserts + numberOfProjectsToAdd) );

    }


}
