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
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestExperimentTest;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestOrganizationTest;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestProjectTest;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ExperimentDTO;
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
                entityStatsDTOPostAdd.getCount() == (projectCountBeforeInserts + numberOfProjectsToAdd));

        PayloadEnvelope<EntityStatsDTO> resultEnvelopePostAddRecheck = gobiiEnvelopeRestResource
                .get(EntityStatsDTO.class);
        EntityStatsDTO entityStatsDTOPostAddRecheck = resultEnvelopePostAddRecheck.getPayload().getData().get(0);

        Assert.assertTrue("The count did not remain the same when no records were added",
                entityStatsDTOPostAddRecheck.getCount() == (projectCountBeforeInserts + numberOfProjectsToAdd));

    }


    @Test
    public void testGetCountOfChildren() throws Exception {

        Integer projectId = (new GlobalPkColl<DtoCrudRequestProjectTest>())
                .getFreshPkVals(DtoCrudRequestProjectTest.class, GobiiEntityNameType.PROJECT,1)
                .get(0);

        RestUri entityCountdUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .entityChildCount(GobiiEntityNameType.PROJECT, GobiiEntityNameType.EXPERIMENT, projectId);

        GobiiEnvelopeRestResource<EntityStatsDTO> gobiiEnvelopeRestResource =
                new GobiiEnvelopeRestResource<>(entityCountdUri);
        PayloadEnvelope<EntityStatsDTO> resultEnvelopeIntialCount = gobiiEnvelopeRestResource
                .get(EntityStatsDTO.class);


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeIntialCount.getHeader()));

        EntityStatsDTO entityStatsDTOInitialCount = resultEnvelopeIntialCount.getPayload().getData().get(0);
        Assert.assertNotNull("Null count retrieved",
                entityStatsDTOInitialCount.getCount()
        );

        Integer countBeforeAddChildren = entityStatsDTOInitialCount.getCount();

        Integer numberoOfChildrenToAdd = 5;

        List<Integer> experimentPks = (new GlobalPkColl<DtoCrudRequestExperimentTest>())
                .getFreshPkVals(DtoCrudRequestExperimentTest.class, GobiiEntityNameType.EXPERIMENT, numberoOfChildrenToAdd);

        for(Integer currentExperimentId : experimentPks ) {
            RestUri experimentsUriById = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceByUriIdParam(GobiiServiceRequestId.URL_EXPERIMENTS);

            experimentsUriById.setParamValue("id",currentExperimentId.toString());
            GobiiEnvelopeRestResource<ExperimentDTO> gobiiEnvelopeRestResourceForExperimentsById = new GobiiEnvelopeRestResource<>(experimentsUriById);
            PayloadEnvelope<ExperimentDTO> resultEnvelopeExperimentGet = gobiiEnvelopeRestResourceForExperimentsById
                    .get(ExperimentDTO.class);
            
            ExperimentDTO currentExperimentDto = resultEnvelopeExperimentGet.getPayload().getData().get(0);
            currentExperimentDto.setProjectId(projectId);

            PayloadEnvelope<ExperimentDTO> putRequestEnvelope = new PayloadEnvelope<>(currentExperimentDto, GobiiProcessType.UPDATE);

            gobiiEnvelopeRestResourceForExperimentsById.put(ExperimentDTO.class,putRequestEnvelope);
        }

        PayloadEnvelope<EntityStatsDTO> postAddChildrenCountResult = gobiiEnvelopeRestResource
                .get(EntityStatsDTO.class);
        
        EntityStatsDTO entityStatsDTOPostAddChildren = postAddChildrenCountResult.getPayload().getData().get(0);
        
        Assert.assertTrue("Child count after adding new children was not incremented",
                entityStatsDTOPostAddChildren.getCount().equals(countBeforeAddChildren + numberoOfChildrenToAdd));

        PayloadEnvelope<EntityStatsDTO> noAddAddChildrenCountResult = gobiiEnvelopeRestResource
                .get(EntityStatsDTO.class);

        EntityStatsDTO entityStatsDTONoAddChildren = noAddAddChildrenCountResult.getPayload().getData().get(0);
        Assert.assertTrue("Child count without adding new children should have remained the same",
                entityStatsDTONoAddChildren.getCount().equals(countBeforeAddChildren + numberoOfChildrenToAdd));


    }


}
