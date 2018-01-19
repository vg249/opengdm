package org.gobiiproject.gobiiclient.gobii.dbops.crud;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvGroupDTO;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by VCalaminos on 1/15/2018.
 */
public class DtoCrudRequestNameIdListTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }

    @Test
    public void testGetCvTermsByGroupAndNameList() throws Exception {

        // get cvGroupId
        RestUri restUriCvGroupDetails = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .cvGroupByQueryParams()
                .setParamValue("groupName", CvGroup.CVGROUP_GERMPLASM_SPECIES.getCvGroupName())
                .setParamValue("cvGroupTypeId", GobiiCvGroupType.GROUP_TYPE_USER.getGroupTypeId().toString());

        GobiiEnvelopeRestResource<CvGroupDTO> cvGroupDTOGobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriCvGroupDetails);
        PayloadEnvelope<CvGroupDTO> cvGroupDTOResultEnvelope = cvGroupDTOGobiiEnvelopeRestResource.get(CvGroupDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvGroupDTOResultEnvelope.getHeader()));
        CvGroupDTO cvGroupDTO = cvGroupDTOResultEnvelope.getPayload().getData().get(0);
        Assert.assertTrue(cvGroupDTO.getCvGroupId() > 0);
        Assert.assertEquals(cvGroupDTO.getName(), CvGroup.CVGROUP_GERMPLASM_SPECIES.getCvGroupName());

        Integer cvGroupId = cvGroupDTO.getCvGroupId();

        // create list of cv terms

        CvDTO newCvDTO1 = TestDtoFactory.makePopulatedCvDTO(GobiiProcessType.CREATE, 1);
        newCvDTO1.setGroupId(cvGroupId);

        CvDTO newCvDTO2 = TestDtoFactory.makePopulatedCvDTO(GobiiProcessType.CREATE, 1);
        newCvDTO2.setGroupId(cvGroupId);


        RestUri restUriCvGroup = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_CVGROUP)
                .addUriParam("groupId")
                .setParamValue("groupId", cvGroupId.toString())
                .appendSegment(GobiiServiceRequestId.URL_CV);

        GobiiEnvelopeRestResource<CvDTO> cvDTOGobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriCvGroup);
        PayloadEnvelope<CvDTO> cvDTOResultEnvelope = cvDTOGobiiEnvelopeRestResource.get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOResultEnvelope.getHeader()));
        List<CvDTO> existingCvDTOList = cvDTOResultEnvelope.getPayload().getData();

        List<String> existingCvTerms = new ArrayList<>();

        if (existingCvDTOList.size() > 0) {

            for (CvDTO currentCvDTO : existingCvDTOList) {
                if (!currentCvDTO.getTerm().equals(null)) {
                    existingCvTerms.add(currentCvDTO.getTerm());
                }
            }
        }

        // check if cv terms already exists; if FALSE, create new CvDTO

        PayloadEnvelope<CvDTO> cvCreatePayloadEnvelope;
        GobiiEnvelopeRestResource<CvDTO> cvCreateGobiiEnvelopeRestResource;
        PayloadEnvelope<CvDTO> cvCreateResultEnvelope;

        if (!existingCvTerms.contains(newCvDTO1.getTerm())) {

            cvCreatePayloadEnvelope = new PayloadEnvelope<>(newCvDTO1, GobiiProcessType.CREATE);
            cvCreateGobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(GobiiServiceRequestId.URL_CV));
            cvCreateResultEnvelope = cvCreateGobiiEnvelopeRestResource.post(CvDTO.class,
                    cvCreatePayloadEnvelope);

            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvCreateResultEnvelope.getHeader()));
            CvDTO cvDTOResponse1 = cvCreateResultEnvelope.getPayload().getData().get(0);
            Assert.assertNotEquals(null, cvDTOResponse1);
            Assert.assertTrue(cvDTOResponse1.getCvId() > 0);

        }

        if (!existingCvTerms.contains(newCvDTO2.getTerm())) {

            cvCreatePayloadEnvelope = new PayloadEnvelope<>(newCvDTO2, GobiiProcessType.CREATE);
            cvCreateGobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(GobiiServiceRequestId.URL_CV));
            cvCreateResultEnvelope = cvCreateGobiiEnvelopeRestResource.post(CvDTO.class,
                    cvCreatePayloadEnvelope);

            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvCreateResultEnvelope.getHeader()));
            CvDTO cvDTOResponse2 = cvCreateResultEnvelope.getPayload().getData().get(0);
            Assert.assertNotEquals(null, cvDTOResponse2);
            Assert.assertTrue(cvDTOResponse2.getCvId() > 0);

        }


        // create NameIdDTOs; 2 existing in the database, and 2 not existing

        NameIdDTO nameIdDTO1 = new NameIdDTO();
        nameIdDTO1.setName(newCvDTO1.getTerm());

        NameIdDTO nameIdDTO2 = new NameIdDTO();
        nameIdDTO2.setName(newCvDTO2.getTerm());

        String notExistingCvTerm = "notexisting_cvterm-" + UUID.randomUUID().toString();

        NameIdDTO nameIdDTONotExisting = new NameIdDTO();
        nameIdDTONotExisting.setName(notExistingCvTerm);

        List<NameIdDTO> nameIdDTOList = new ArrayList<>();
        nameIdDTOList.add(nameIdDTO1);
        nameIdDTOList.add(nameIdDTO2);
        nameIdDTOList.add(nameIdDTONotExisting);

        PayloadEnvelope<NameIdDTO> payloadEnvelope = new PayloadEnvelope<>();
        payloadEnvelope.getHeader().setGobiiProcessType(GobiiProcessType.CREATE);
        payloadEnvelope.getPayload().setData(nameIdDTOList);


        RestUri namesUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .nameIdListByQueryParams();
        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(namesUri);
        namesUri.setParamValue("entity", GobiiEntityNameType.CV.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.NAMES_BY_NAME_LIST.toString().toUpperCase()));
        namesUri.setParamValue("filterValue", CvGroup.CVGROUP_GERMPLASM_SPECIES.getCvGroupName());
        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = gobiiEnvelopeRestResource.post(NameIdDTO.class, payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(responsePayloadEnvelope.getHeader()));

        List<NameIdDTO> nameIdDTOListResponse = responsePayloadEnvelope.getPayload().getData();
        Assert.assertNotEquals(null, nameIdDTOListResponse);
        Assert.assertEquals(nameIdDTOList.size(), nameIdDTOListResponse.size());



    }

}
