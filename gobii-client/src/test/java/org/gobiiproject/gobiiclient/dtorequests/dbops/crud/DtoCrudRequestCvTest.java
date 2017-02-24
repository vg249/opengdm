// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.dbops.crud;

import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.*;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DtoCrudRequestCvTest implements DtoCrudRequestTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }



    @Test
    @Override
    public void get() throws Exception {
        RestUri restUriCv = ClientContext.getInstance(null,false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_CV);
        GobiiEnvelopeRestResource<CvDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriCv);
        PayloadEnvelope<CvDTO> resultEnvelope = gobiiEnvelopeRestResource.get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<CvDTO> cvDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(cvDTOList);
        Assert.assertTrue(cvDTOList.size() > 0);
        Assert.assertNotNull(cvDTOList.get(0).getTerm());

        // use an arbitrary cv id
        Integer cvId = cvDTOList.get(0).getCvId();
        RestUri restUriCvForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_CV);
        restUriCvForGetById.setParamValue("id", cvId.toString());
        GobiiEnvelopeRestResource<CvDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriCvForGetById);
        PayloadEnvelope<CvDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById
                .get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        CvDTO cvDTO = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(cvDTO.getCvId() > 0);
        Assert.assertNotNull(cvDTO.getTerm());

    }

    @Test
    @Override
    public void testEmptyResult() throws Exception {

        DtoRestRequestUtils<CvDTO> dtoDtoRestRequestUtils = new DtoRestRequestUtils<>(CvDTO.class, ServiceRequestId.URL_CV);
        Integer maxId = dtoDtoRestRequestUtils.getMaxPkVal();
        Integer nonExistentID = maxId + 1;

        PayloadEnvelope<CvDTO> resultEnvelope = dtoDtoRestRequestUtils.getResponseEnvelopeForEntityId(nonExistentID.toString());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertNotNull(resultEnvelope.getPayload());
        Assert.assertNotNull(resultEnvelope.getPayload().getData());
        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0);

    }


    @Test
    @Override
    public void create() throws Exception {

        CvDTO newCvDto = TestDtoFactory
                .makePopulatedCvDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<CvDTO> payloadEnvelope = new PayloadEnvelope<>(newCvDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<CvDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_CV));
        PayloadEnvelope<CvDTO> cvDTOResponseEnvelope = gobiiEnvelopeRestResource.post(CvDTO.class,
                payloadEnvelope);
        CvDTO cvDTOResponse = cvDTOResponseEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, cvDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOResponseEnvelope.getHeader()));
        Assert.assertTrue(cvDTOResponse.getCvId() > 0);

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.CVTERMS,
                cvDTOResponse.getCvId());


        RestUri restUriCvForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_CV);
        restUriCvForGetById.setParamValue("id", cvDTOResponse.getCvId().toString());
        GobiiEnvelopeRestResource<CvDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriCvForGetById);
        PayloadEnvelope<CvDTO> resultEnvelopeForGetByID = restResourceForGetById
                .get(CvDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        CvDTO cvDTOResponseForParams = resultEnvelopeForGetByID.getPayload().getData().get(0);

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.CVTERMS, cvDTOResponse.getCvId());

    }

    @Test
    public void createCvForSystemGroup() throws Exception {

        CvDTO newCvDto = TestDtoFactory
                .makePopulatedCvDTO(GobiiProcessType.CREATE, 1);

        newCvDto.setGroupId(1);

        PayloadEnvelope<CvDTO> payloadEnvelope = new PayloadEnvelope<>(newCvDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<CvDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_CV));
        PayloadEnvelope<CvDTO> cvDTOResponseEnvelope = gobiiEnvelopeRestResource.post(CvDTO.class,
                payloadEnvelope);

        Assert.assertTrue(TestUtils.checkAndPrintHeaderMessages(cvDTOResponseEnvelope.getHeader()));

        Assert.assertTrue("The error message should contain 'belongs to a cvgroup of type system'",
            cvDTOResponseEnvelope.getHeader()
                    .getStatus()
                    .getStatusMessages()
                    .stream()
                    .filter(m -> m.getMessage().toLowerCase().contains("belongs to a cvgroup of type system"))
                    .count()
                    > 0);

        Assert.assertTrue(cvDTOResponseEnvelope.getPayload().getData().size() == 0);

    }



    @Test
    @Override
    public void update() throws Exception {

        // create a new cv for our test
        CvDTO newCvDto = TestDtoFactory
                .makePopulatedCvDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<CvDTO> payloadEnvelope = new PayloadEnvelope<>(newCvDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<CvDTO> restResource = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_CV));
        PayloadEnvelope<CvDTO> protocolDTOResponseEnvelope = restResource.post(CvDTO.class,
                payloadEnvelope);
        CvDTO newCvDTOResponse = protocolDTOResponseEnvelope.getPayload().getData().get(0);

        // re-retrieve the cv we just created so we start with a fresh READ mode dto

        RestUri restUriCvForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_CV);
        restUriCvForGetById.setParamValue("id", newCvDTOResponse.getCvId().toString());
        GobiiEnvelopeRestResource<CvDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriCvForGetById);
        PayloadEnvelope<CvDTO> resultEnvelopeForGetByID = restResourceForGetById
                .get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        CvDTO cvDTOReceived = resultEnvelopeForGetByID.getPayload().getData().get(0);


        String newName = UUID.randomUUID().toString();
        cvDTOReceived.setTerm(newName);
        restResourceForGetById.setParamValue("id", cvDTOReceived.getCvId().toString());
        PayloadEnvelope<CvDTO> cvDTOResponseEnvelopeUpdate = restResourceForGetById.put(CvDTO.class,
                new PayloadEnvelope<>(cvDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOResponseEnvelopeUpdate.getHeader()));

        CvDTO CvDTORequest = cvDTOResponseEnvelopeUpdate.getPayload().getData().get(0);


        restUriCvForGetById.setParamValue("id", CvDTORequest.getCvId().toString());
        resultEnvelopeForGetByID = restResourceForGetById
                .get(CvDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));


        CvDTO dtoRequestCvReRetrieved = resultEnvelopeForGetByID.getPayload().getData().get(0);


        Assert.assertTrue(dtoRequestCvReRetrieved.getTerm().equals(newName));

    }


    @Test
    public void updateCvForSystemGroup() throws Exception {

        // create a new cv for our test
        CvDTO newCvDto = TestDtoFactory
                .makePopulatedCvDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<CvDTO> payloadEnvelope = new PayloadEnvelope<>(newCvDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<CvDTO> restResource = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_CV));
        PayloadEnvelope<CvDTO> protocolDTOResponseEnvelope = restResource.post(CvDTO.class,
                payloadEnvelope);
        CvDTO newCvDTOResponse = protocolDTOResponseEnvelope.getPayload().getData().get(0);

        // re-retrieve the cv we just created so we start with a fresh READ mode dto

        RestUri restUriCvForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_CV);
        restUriCvForGetById.setParamValue("id", newCvDTOResponse.getCvId().toString());
        GobiiEnvelopeRestResource<CvDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriCvForGetById);
        PayloadEnvelope<CvDTO> resultEnvelopeForGetByID = restResourceForGetById
                .get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        CvDTO cvDTOReceived = resultEnvelopeForGetByID.getPayload().getData().get(0);

        String currentName = cvDTOReceived.getTerm();

        // change group ID and group type to system group
        cvDTOReceived.setGroupId(1);
        cvDTOReceived.setGroupType(1);

        restResourceForGetById.setParamValue("id", cvDTOReceived.getCvId().toString());
        PayloadEnvelope<CvDTO> cvDTOResponseEnvelopeUpdateSystemGroup = restResourceForGetById.put(CvDTO.class,
                new PayloadEnvelope<>(cvDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOResponseEnvelopeUpdateSystemGroup.getHeader()));


        String newName = UUID.randomUUID().toString();
        cvDTOReceived.setTerm(newName);
        restResourceForGetById.setParamValue("id", cvDTOReceived.getCvId().toString());
        PayloadEnvelope<CvDTO> cvDTOResponseEnvelopeUpdate = restResourceForGetById.put(CvDTO.class,
                new PayloadEnvelope<>(cvDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertTrue(TestUtils.checkAndPrintHeaderMessages(cvDTOResponseEnvelopeUpdate.getHeader()));

        Assert.assertTrue("The error message should contain 'belongs to a cvgroup of type system'",
            cvDTOResponseEnvelopeUpdate.getHeader()
                    .getStatus()
                    .getStatusMessages()
                    .stream()
                    .filter(m -> m.getMessage().toLowerCase().contains("belongs to a cvgroup of type system"))
                    .count()
                    > 0);


        Assert.assertTrue(cvDTOResponseEnvelopeUpdate.getPayload().getData().size() == 0);


        restUriCvForGetById.setParamValue("id", cvDTOReceived.getCvId().toString());
        resultEnvelopeForGetByID = restResourceForGetById
                .get(CvDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));


        CvDTO dtoRequestCvReRetrieved = resultEnvelopeForGetByID.getPayload().getData().get(0);

        //check that the cv term has not been updated
        Assert.assertTrue(dtoRequestCvReRetrieved.getTerm().equals(currentName));

    }


    @Test
    public void delete() throws Exception {

        // create a new cv for our test
        CvDTO newCvDto = TestDtoFactory
                .makePopulatedCvDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<CvDTO> payloadEnvelope = new PayloadEnvelope<>(newCvDto, GobiiProcessType.CREATE);

        GobiiEnvelopeRestResource<CvDTO> restResource = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_CV));

        PayloadEnvelope<CvDTO> protocolDTOResponseEnvelope = restResource.post(CvDTO.class,
                payloadEnvelope);

        CvDTO newCvDTOResponse = protocolDTOResponseEnvelope.getPayload().getData().get(0);

        Integer newCvId = newCvDTOResponse.getCvId();

        // re-retrieve the cv we just created so we start with a fresh READ mode too

        RestUri restUriCvForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_CV);

        restUriCvForGetById.setParamValue("id", newCvDTOResponse.getCvId().toString());
        GobiiEnvelopeRestResource<CvDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriCvForGetById);
        PayloadEnvelope<CvDTO> resultEnvelopeForGetById = restResourceForGetById
                .get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        CvDTO cvDTOReceived = resultEnvelopeForGetById.getPayload().getData().get(0);

        restResourceForGetById.setParamValue("id", cvDTOReceived.getCvId().toString());

        PayloadEnvelope<CvDTO> cvDTOResponseEnvelopeDelete = restResourceForGetById.delete(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOResponseEnvelopeDelete.getHeader()));


        // check if cv has been deleted

        RestUri restUriCvForGetByIdDeleted = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_CV);
        restUriCvForGetByIdDeleted.setParamValue("id", newCvId.toString());
        GobiiEnvelopeRestResource<CvDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriCvForGetByIdDeleted);
        PayloadEnvelope<CvDTO> resultEnvelopeForGetByIdDeleted = gobiiEnvelopeRestResourceForGetById
                .get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByIdDeleted.getHeader()));
        Integer responseSize = resultEnvelopeForGetByIdDeleted.getPayload().getData().size();
        Assert.assertTrue(responseSize <= 0);


    }



    @Test
    public void deleteCvForSystemGroup() throws Exception {

        // create a new cv for our test
        CvDTO newCvDto = TestDtoFactory
                .makePopulatedCvDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<CvDTO> payloadEnvelope = new PayloadEnvelope<>(newCvDto, GobiiProcessType.CREATE);

        GobiiEnvelopeRestResource<CvDTO> restResource = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_CV));

        PayloadEnvelope<CvDTO> protocolDTOResponseEnvelope = restResource.post(CvDTO.class,
                payloadEnvelope);

        CvDTO newCvDTOResponse = protocolDTOResponseEnvelope.getPayload().getData().get(0);

        Integer newCvId = newCvDTOResponse.getCvId();

        // re-retrieve the cv we just created so we start with a fresh READ mode too

        RestUri restUriCvForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_CV);

        restUriCvForGetById.setParamValue("id", newCvDTOResponse.getCvId().toString());
        GobiiEnvelopeRestResource<CvDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriCvForGetById);
        PayloadEnvelope<CvDTO> resultEnvelopeForGetById = restResourceForGetById
                .get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        CvDTO cvDTOReceived = resultEnvelopeForGetById.getPayload().getData().get(0);


        // change group ID and group type to system group
        cvDTOReceived.setGroupId(1);
        cvDTOReceived.setGroupType(1);

        restResourceForGetById.setParamValue("id", cvDTOReceived.getCvId().toString());
        PayloadEnvelope<CvDTO> cvDTOResponseEnvelopeUpdateSystemGroup = restResourceForGetById.put(CvDTO.class,
                new PayloadEnvelope<>(cvDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOResponseEnvelopeUpdateSystemGroup.getHeader()));

        restResourceForGetById.setParamValue("id", cvDTOReceived.getCvId().toString());

        PayloadEnvelope<CvDTO> cvDTOResponseEnvelopeDelete = restResourceForGetById.delete(CvDTO.class);

        Assert.assertTrue(TestUtils.checkAndPrintHeaderMessages(cvDTOResponseEnvelopeDelete.getHeader()));


        Assert.assertTrue("The error message should contain 'belongs to a cvgroup of type system'",
            cvDTOResponseEnvelopeDelete.getHeader()
                    .getStatus()
                    .getStatusMessages()
                    .stream()
                    .filter(m -> m.getMessage().toLowerCase().contains("belongs to a cvgroup of type system"))
                    .count()
                    > 0);

        // the cv should not be deleted

        RestUri restUriCvForGetByIdDeleted = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_CV);
        restUriCvForGetByIdDeleted.setParamValue("id", newCvId.toString());
        GobiiEnvelopeRestResource<CvDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriCvForGetByIdDeleted);
        PayloadEnvelope<CvDTO> resultEnvelopeForGetByIdDeleted = gobiiEnvelopeRestResourceForGetById
                .get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByIdDeleted.getHeader()));
        Integer responseSize = resultEnvelopeForGetByIdDeleted.getPayload().getData().size();
        Assert.assertTrue(responseSize > 0);


    }


    @Test
    @Override
    public void getList() throws Exception {

        RestUri restUriMapset = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_CV);
        GobiiEnvelopeRestResource<CvDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriMapset);
        PayloadEnvelope<CvDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<CvDTO> cvDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(cvDTOList);
        Assert.assertTrue(cvDTOList.size() > 0);
        Assert.assertNotNull(cvDTOList.get(0).getTerm());

        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == cvDTOList.size());

        List<Integer> itemsToTest = new ArrayList<>();
        if (cvDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, cvDTOList.size());
        } else {
            for (int idx = 0; idx < cvDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentIdx : itemsToTest) {
            CvDTO currentCvDto = cvDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriCvForGetById = ClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<CvDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriCvForGetById);
            PayloadEnvelope<CvDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById
                    .get(CvDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetById);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
            CvDTO cvDTOFromLink = resultEnvelopeForGetById.getPayload().getData().get(0);
            Assert.assertTrue(currentCvDto.getTerm().equals(cvDTOFromLink.getTerm()));
            Assert.assertTrue(currentCvDto.getCvId().equals(cvDTOFromLink.getCvId()));
        }

    }
}
