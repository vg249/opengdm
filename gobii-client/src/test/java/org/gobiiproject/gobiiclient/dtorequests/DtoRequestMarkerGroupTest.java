// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupMarkerDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.gobiiproject.gobiimodel.dto.header.HeaderStatusMessage;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DtoRequestMarkerGroupTest {

    private List<String> validMarkerNames = Arrays.asList("4806",
            "4824",
            "4831",
            "7925",
            "8144",
            "9614",
            "9673",
            "10710",
            "14005",
            "16297",
            "19846",
            "20215");


    @Test
    public void testMarkerGroupCreate() throws Exception {

        DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();


        List<MarkerGroupMarkerDTO> markerGroupMarkers = TestDtoFactory.makeMarkerGroupMarkers(validMarkerNames,
                DtoMetaData.ProcessType.CREATE);

        MarkerGroupDTO markerGroupDTORequest = TestDtoFactory
                .makePopulatedMarkerGroupDTO(DtoMetaData.ProcessType.CREATE, 1, markerGroupMarkers);

        MarkerGroupDTO markerGroupDTOResponse = dtoRequestMarkerGroup.process(markerGroupDTORequest);

        Assert.assertNotEquals(null, markerGroupDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(markerGroupDTOResponse));
        Assert.assertTrue(markerGroupDTOResponse.getMarkerGroupId() > 1);

        Assert.assertNotNull(markerGroupDTOResponse.getMarkers());

        Integer totalMarkersWithMarkerAndPlatformIds = markerGroupDTOResponse
                .getMarkers()
                .stream()
                .filter(m -> (m.getMarkerId() > 0) && (m.getPlatformId() > 0))
                .collect(Collectors.toList())
                .size();

        Assert.assertTrue(totalMarkersWithMarkerAndPlatformIds == markerGroupDTORequest.getMarkers().size());


    }

    @Test
    public void testMarkerGroupCreateFailSomeMarkers() throws Exception {

        DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();

        List<String> someInvalidNames = new ArrayList<>(validMarkerNames);
        someInvalidNames.add("i-do-not-exist!");


        List<MarkerGroupMarkerDTO> markerGroupMarkers = TestDtoFactory.makeMarkerGroupMarkers(someInvalidNames,
                DtoMetaData.ProcessType.CREATE);

        MarkerGroupDTO markerGroupDTORequest = TestDtoFactory
                .makePopulatedMarkerGroupDTO(DtoMetaData.ProcessType.CREATE, 1, markerGroupMarkers);

        MarkerGroupDTO markerGroupDTOResponse = dtoRequestMarkerGroup.process(markerGroupDTORequest);

        Assert.assertNotEquals(null, markerGroupDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(markerGroupDTOResponse));

        Assert.assertTrue(markerGroupDTOResponse.getMarkerGroupId() > 0);

        Assert.assertNotNull(markerGroupDTOResponse.getMarkers());

        Integer totalInvalidMarkers = markerGroupDTOResponse
                .getMarkers()
                .stream()
                .filter(m -> !m.isMarkerExists()
                        && (m.getMarkerId() == null)
                        && (m.getPlatformName() == null)
                        && (m.getPlatformId() == null))

                .collect(Collectors.toList())
                .size();

        Assert.assertTrue(totalInvalidMarkers == 1);


    }

    @Test
    public void testMarkerGroupCreateFailAllMarkers() throws Exception {

        DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();

        List<String> allInvalidNames = new ArrayList<>();
        allInvalidNames.add("i-do-not-exist 1");
        allInvalidNames.add("i-do-not-exist 2");
        allInvalidNames.add("i-do-not-exist 3");
        allInvalidNames.add("i-do-not-exist 4");
        allInvalidNames.add("i-do-not-exist 5");


        List<MarkerGroupMarkerDTO> markerGroupMarkers = TestDtoFactory.makeMarkerGroupMarkers(allInvalidNames,
                DtoMetaData.ProcessType.CREATE);

        MarkerGroupDTO markerGroupDTORequest = TestDtoFactory
                .makePopulatedMarkerGroupDTO(DtoMetaData.ProcessType.CREATE, 1, markerGroupMarkers);

        MarkerGroupDTO markerGroupDTOResponse = dtoRequestMarkerGroup.process(markerGroupDTORequest);

        Assert.assertNotEquals(null, markerGroupDTOResponse);
        Assert.assertFalse(markerGroupDTOResponse.getDtoHeaderResponse().isSucceeded());

        Assert.assertTrue(null == markerGroupDTOResponse.getMarkerGroupId() || markerGroupDTOResponse.getMarkerGroupId() > 0);

        List<HeaderStatusMessage> invalidResponses = markerGroupDTOResponse
                .getDtoHeaderResponse()
                .getStatusMessages()
                .stream()
                .filter(m -> m.getValidationStatusType() == DtoHeaderResponse.ValidationStatusType.NONEXISTENT_FK_ENTITY )
                .collect(Collectors.toList());

        Assert.assertTrue(invalidResponses.size() == 1);

    }


    @Ignore
    public void testMarkerGroupGet() throws Exception {


        DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();
        MarkerGroupDTO markerGroupDTORequest = new MarkerGroupDTO();
        markerGroupDTORequest.setMarkerGroupId(1);
        MarkerGroupDTO markerGroupDTOResponse = dtoRequestMarkerGroup.process(markerGroupDTORequest);

        Assert.assertNotEquals(null, markerGroupDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(markerGroupDTOResponse));
        Assert.assertNotEquals(null, markerGroupDTOResponse.getName());


    }


    @Ignore
    public void testUpdateMarkerGroup() throws Exception {

//        DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();
//
//        // create a new markerGroup for our test
//        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
//        MarkerGroupDTO newMarkerGroupDto = TestDtoFactory
//                .makePopulatedMarkerGroupDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);
//        MarkerGroupDTO newMarkerGroupDTOResponse = dtoRequestMarkerGroup.process(newMarkerGroupDto);
//
//
//        // re-retrieve the markerGroup we just created so we start with a fresh READ mode dto
//        MarkerGroupDTO MarkerGroupDTORequest = new MarkerGroupDTO();
//        MarkerGroupDTORequest.setMarkerGroupId(newMarkerGroupDTOResponse.getMarkerGroupId());
//        MarkerGroupDTO markerGroupDTOReceived = dtoRequestMarkerGroup.process(MarkerGroupDTORequest);
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(markerGroupDTOReceived));
//
//
//        // so this would be the typical workflow for the client app
//        markerGroupDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);
//        String newDataFile = UUID.randomUUID().toString();
//        markerGroupDTOReceived.setSourceName(newDataFile);
//
//        EntityPropertyDTO propertyToUpdate = markerGroupDTOReceived.getParameters().get(0);
//        String updatedPropertyName = propertyToUpdate.getPropertyName();
//        String updatedPropertyValue = UUID.randomUUID().toString();
//        propertyToUpdate.setPropertyValue(updatedPropertyValue);
//
//        MarkerGroupDTO MarkerGroupDTOResponse = dtoRequestMarkerGroup.process(markerGroupDTOReceived);
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(MarkerGroupDTOResponse));
//
//
//        MarkerGroupDTO dtoRequestMarkerGroupReRetrieved =
//                dtoRequestMarkerGroup.process(MarkerGroupDTORequest);
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestMarkerGroupReRetrieved));
//
//        Assert.assertTrue(dtoRequestMarkerGroupReRetrieved.getSourceName().equals(newDataFile));
//
//        EntityPropertyDTO matchedProperty = dtoRequestMarkerGroupReRetrieved
//                .getParameters()
//                .stream()
//                .filter(m -> m.getPropertyName().equals(updatedPropertyName) )
//                .collect(Collectors.toList())
//                .get(0);
//
//        Assert.assertTrue(matchedProperty.getPropertyValue().equals(updatedPropertyValue));
//
    }

}
