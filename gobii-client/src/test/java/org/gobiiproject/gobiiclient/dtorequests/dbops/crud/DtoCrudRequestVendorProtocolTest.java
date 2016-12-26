package org.gobiiproject.gobiiclient.dtorequests.dbops.crud;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.DtoRestRequestUtils;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.GlobalPkValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProjectDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProtocolDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by VCalaminos on 2016-12-14.
 */

public class DtoCrudRequestVendorProtocolTest implements DtoCrudRequestTest {

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
    public void create() throws Exception {

        // ********** SET UP THE PROTOCOL
        Integer protocolId = (new GlobalPkColl<DtoCrudRequestProtocolTest>()
                .getAPkVal(DtoCrudRequestProtocolTest.class, GobiiEntityNameType.PROTOCOLS));
        RestUri restUriForGetProtocolById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_PROTOCOL);
        restUriForGetProtocolById.setParamValue("id", protocolId.toString());
        GobiiEnvelopeRestResource<ProtocolDTO> gobiiEnvelopeRestResourceForGetProtocolById =
                new GobiiEnvelopeRestResource<>(restUriForGetProtocolById);
        PayloadEnvelope<ProtocolDTO> resultEnvelopeForGetProtocolByID = gobiiEnvelopeRestResourceForGetProtocolById
                .get(ProtocolDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetProtocolByID.getHeader()));
        ProtocolDTO protocolDTO = resultEnvelopeForGetProtocolByID.getPayload().getData().get(0);


        // ********** SET UP THE ORGANIZATION FOR THE VENDOR
        Integer organizationId = (new GlobalPkColl<DtoCrudRequestOrganizationTest>()
                .getAPkVal(DtoCrudRequestOrganizationTest.class, GobiiEntityNameType.ORGANIZATIONS));

        RestUri restUriForGetOrganizationById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_ORGANIZATION);
        restUriForGetOrganizationById.setParamValue("id", organizationId.toString());
        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResourceForGetOrganizationById =
                new GobiiEnvelopeRestResource<>(restUriForGetOrganizationById);
        PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetOrganizationByID = gobiiEnvelopeRestResourceForGetOrganizationById
                .get(OrganizationDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetOrganizationByID.getHeader()));
        OrganizationDTO organizationDTO = resultEnvelopeForGetOrganizationByID.getPayload().getData().get(0);

        // ********** PREDICT RESULTING NAME
        String protocolName = protocolDTO.getName();
        String organizationName = organizationDTO.getName();
        String predictedVendorProtocolName = organizationName + "_" + protocolName;

        // ********** POST VENDOR ORGANIZATION TO PROTOCOL
        RestUri restUriProtocoLVendor = ClientContext.getInstance(null, false)
                .getUriFactory()
                .childResourceByUriIdParam(ServiceRequestId.URL_PROTOCOL,
                        ServiceRequestId.URL_VENDORS);
        restUriProtocoLVendor.setParamValue("id", protocolId.toString());
        GobiiEnvelopeRestResource<OrganizationDTO> protocolVendorResource =
                new GobiiEnvelopeRestResource<>(restUriProtocoLVendor);
        PayloadEnvelope<OrganizationDTO> vendorPayloadEnvelope =
                new PayloadEnvelope<>(organizationDTO, GobiiProcessType.CREATE);
        PayloadEnvelope<OrganizationDTO> protocolVendorResult =
                protocolVendorResource.post(OrganizationDTO.class, vendorPayloadEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(protocolVendorResult.getHeader()));

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.VENDORS_PROTOCOLS, protocolDTO.getProtocolId());


        // ************ VERIFY THAT VENDOR-PROTOCOL WAS CREATED
        RestUri namesUri = ClientContext.getInstance(null, false)
                .getUriFactory()
                .nameIdListByQueryParams();
        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(namesUri);
        namesUri.setParamValue("entity", GobiiEntityNameType.VENDORS_PROTOCOLS.toString().toLowerCase());

        PayloadEnvelope<NameIdDTO> resultEnvelopeProtocoLVendornames = gobiiEnvelopeRestResource
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeProtocoLVendornames.getHeader()));

        List<NameIdDTO> nameIdDTOs = resultEnvelopeProtocoLVendornames.getPayload().getData();

        Assert.assertTrue(nameIdDTOs.size() > 0);

        Assert.assertTrue(nameIdDTOs
                .stream()
                .filter(nameIdDTO -> nameIdDTO.getName().toLowerCase().equals(predictedVendorProtocolName))
                .count() == 1);


        // ************* VERIFY THAT REPOSTING THE SAME VENDOR TO THE SAME PROTOCOL GIVES A VALIDATION ERROR
        protocolVendorResult =
                protocolVendorResource.post(OrganizationDTO.class, vendorPayloadEnvelope);

        Assert.assertTrue(
                protocolVendorResult
                        .getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(headerStatusMessage ->
                                headerStatusMessage
                                        .getGobiiStatusLevel().equals(GobiiStatusLevel.VALIDATION) &&
                                        headerStatusMessage
                                                .getGobiiValidationStatusType()
                                                .equals(GobiiValidationStatusType.ENTITY_ALREADY_EXISTS)
                        ).count() == 1);


        // ************* VERIFY THAT WE CAN RETRIEVE THE CREATED VENDOR THROUGH THE PROTOCOLS URL
//        RestUri restUriOrganizationThroughProtocol = ClientContext.getInstance(null, false)
//                .getUriFactory()
//                .resourceColl(ServiceRequestId.URL_PROTOCOL)
//                .addUriParam("id")
//                .appendSegment(ServiceRequestId.URL_VENDORS)
//                .addUriParam("vendorProtocolName");
//        restUriOrganizationThroughProtocol.setParamValue("id", protocolId.toString());
//        restUriOrganizationThroughProtocol.setParamValue("vendorProtocolName", predictedVendorProtocolName);
//
//        GobiiEnvelopeRestResource<OrganizationDTO> protocolVendorResourceForOrganizationThroughProtocol =
//                new GobiiEnvelopeRestResource<>(restUriOrganizationThroughProtocol);
//        protocolVendorResult =
//                protocolVendorResourceForOrganizationThroughProtocol.get(OrganizationDTO.class);
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(protocolVendorResult.getHeader()));
//        OrganizationDTO organizationDTOThroughProtocol = protocolVendorResult.getPayload().getData().get(0);
//        Assert.assertTrue(organizationDTOThroughProtocol.getOrganizationId().equals(organizationDTO.getOrganizationId()));


        //need services for GET /protocols/{id}/vendors/
        //                  GET /protocols/{id}/vendors/{protocol_vendor_name}

    }


    @Test
    @Override
    public void update() throws Exception {
    }


    @Test
    @Override
    public void testEmptyResult() throws Exception {
    }


    @Test
    @Override
    public void get() throws Exception {
    }


    @Test
    @Override
    public void getList() throws Exception {

        Integer protocolId = (new GlobalPkColl<DtoCrudRequestVendorProtocolTest>()
                .getAPkVal(DtoCrudRequestVendorProtocolTest.class, GobiiEntityNameType.VENDORS_PROTOCOLS));

        RestUri restUriVendorsForProtocol = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_PROTOCOL)
                .addUriParam("protocolId")
                .setParamValue("protocolId",protocolId.toString())
                .appendSegment(ServiceRequestId.URL_VENDORS);

        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriVendorsForProtocol);
        PayloadEnvelope<OrganizationDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(OrganizationDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<OrganizationDTO> organizationDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(organizationDTOList);
        Assert.assertTrue(organizationDTOList.size() > 0);
        Assert.assertNotNull(organizationDTOList.get(0).getName());


//        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
//        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == organizationDTOList.size());
//
//        List<Integer> itemsToTest = new ArrayList<>();
//        if (organizationDTOList.size() > 50) {
//            itemsToTest = TestUtils.makeListOfIntegersInRange(10, organizationDTOList.size());
//
//        } else {
//            for (int idx = 0; idx < organizationDTOList.size(); idx++) {
//                itemsToTest.add(idx);
//            }
//        }
//
//        for (Integer currentIdx : itemsToTest) {
//            ProjectDTO currentProjectDto = projectDTOList.get(currentIdx);
//
//            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);
//
//            RestUri restUriProjectForGetById = ClientContext.getInstance(null, false)
//                    .getUriFactory()
//                    .RestUriFromUri(currentLink.getHref());
//            GobiiEnvelopeRestResource<ProjectDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriProjectForGetById);
//            PayloadEnvelope<ProjectDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
//                    .get(ProjectDTO.class);
//            Assert.assertNotNull(resultEnvelopeForGetByID);
//            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
//            ProjectDTO projectDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);
//            Assert.assertTrue(currentProjectDto.getProjectName().equals(projectDTOFromLink.getProjectName()));
//            Assert.assertTrue(currentProjectDto.getProjectId().equals(projectDTOFromLink.getProjectId()));
//        }

    }

}
