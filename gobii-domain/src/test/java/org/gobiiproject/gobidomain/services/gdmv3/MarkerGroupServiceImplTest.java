package org.gobiiproject.gobidomain.services.gdmv3;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobidomain.services.gdmv3.exceptions.InvalidMarkersException;
import org.gobiiproject.gobidomain.services.gdmv3.exceptions.MarkerStatus;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerDTO;
import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.entity.MarkerGroup;
import org.gobiiproject.gobiimodel.entity.Platform;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.MarkerDao;
import org.gobiiproject.gobiisampletrackingdao.MarkerGroupDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import lombok.extern.slf4j.Slf4j;

@WebAppConfiguration
@Slf4j
public class MarkerGroupServiceImplTest {

    @Mock
    private CvDao cvDao;

    @Mock
    private MarkerGroupDao markerGroupDao;

    @Mock
    private ContactDao contactDao;

    @Mock
    private MarkerDao markerDao;

    @InjectMocks
    private MarkerGroupServiceImpl markerGroupServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvalidAlleles() {

        MarkerDTO markerDTO = new MarkerDTO();
        markerDTO.setMarkerName("test1");
        markerDTO.setPlatformName("KASP");
        markerDTO.setFavorableAlleles(new String[]{ "B", "D", "E"});

        List<MarkerDTO> inputMarkers = new ArrayList<>();
        inputMarkers.add(markerDTO);

        MarkerGroup mockMarkerGroup = new MarkerGroup();
        mockMarkerGroup.setMarkerGroupId(123);
        mockMarkerGroup.setName("test-marker-group");
        mockMarkerGroup.setGermplasmGroup("test-germplasm-group");
        
        when( markerGroupDao.getMarkerGroup(123)).thenReturn(
            mockMarkerGroup
        );

        List<Marker> mockMarkers = new ArrayList<>();
        Marker mockMarker = new Marker();
        mockMarker.setMarkerName("test1");
        
        Platform mockPlatform = new Platform();
        mockPlatform.setPlatformId(1);
        mockPlatform.setPlatformName("KASP");
        mockMarker.setPlatform(mockPlatform);

        mockMarkers.add(mockMarker);

        when ( markerDao.getMarkersByPlatformMarkerNameTuples( anyList())).thenReturn(
            mockMarkers
        );


        Exception exception = assertThrows(InvalidMarkersException.class, () -> {
            markerGroupServiceImpl.mapMarkers(123, inputMarkers, "test-user" );
        });

        assertTrue("Exception is null", exception != null);

        InvalidMarkersException exc = (InvalidMarkersException) exception;

        List<MarkerStatus> statusList = exc.getStatusList();
        assertTrue("Status list empty", statusList != null &&  statusList.size() == 1);
       
        
        MarkerStatus status = statusList.get(0);
        log.info(status.getError());
        assertTrue( status.getError().equals("Bad Request. Invalid allele value(s) for test1: B, D, E"));

    }

    @Test
    public void testInvalidAlleles2() {

        MarkerDTO markerDTO = new MarkerDTO();
        markerDTO.setMarkerName("test1");
        markerDTO.setPlatformName("KASP");
        markerDTO.setFavorableAlleles(new String[]{ "A", "G", "C", "T", "Z", "1000", "100", "0900", "+", "-", "_", "2000"});

        List<MarkerDTO> inputMarkers = new ArrayList<>();
        inputMarkers.add(markerDTO);

        MarkerGroup mockMarkerGroup = new MarkerGroup();
        mockMarkerGroup.setMarkerGroupId(123);
        mockMarkerGroup.setName("test-marker-group");
        mockMarkerGroup.setGermplasmGroup("test-germplasm-group");
        
        when( markerGroupDao.getMarkerGroup(123)).thenReturn(
            mockMarkerGroup
        );

        List<Marker> mockMarkers = new ArrayList<>();
        Marker mockMarker = new Marker();
        mockMarker.setMarkerName("test1");
        
        Platform mockPlatform = new Platform();
        mockPlatform.setPlatformId(1);
        mockPlatform.setPlatformName("KASP");
        mockMarker.setPlatform(mockPlatform);

        mockMarkers.add(mockMarker);

        when ( markerDao.getMarkersByPlatformMarkerNameTuples( anyList())).thenReturn(
            mockMarkers
        );


        Exception exception = assertThrows(InvalidMarkersException.class, () -> {
            markerGroupServiceImpl.mapMarkers(123, inputMarkers, "test-user" );
        });

        assertTrue("Exception is null", exception != null);

        InvalidMarkersException exc = (InvalidMarkersException) exception;

        List<MarkerStatus> statusList = exc.getStatusList();
        assertTrue("Status list empty", statusList != null &&  statusList.size() == 1);
       
        
        MarkerStatus status = statusList.get(0);
        log.info(status.getError());
        assertTrue( status.getError().equals("Bad Request. Invalid allele value(s) for test1: Z, 100, _, 2000"));

    }

    @Test
    public void testMissingMarker1() {
        MarkerDTO markerDTO = new MarkerDTO();
        markerDTO.setMarkerName("test1");
        markerDTO.setPlatformName("KASP");
        markerDTO.setFavorableAlleles(new String[]{ "G" });

        List<MarkerDTO> inputMarkers = new ArrayList<>();
        inputMarkers.add(markerDTO);

        MarkerGroup mockMarkerGroup = new MarkerGroup();
        mockMarkerGroup.setMarkerGroupId(123);
        mockMarkerGroup.setName("test-marker-group");
        mockMarkerGroup.setGermplasmGroup("test-germplasm-group");
        
        when( markerGroupDao.getMarkerGroup(123)).thenReturn(
            mockMarkerGroup
        );

        List<Marker> mockMarkers = new ArrayList<>();
        //no matching marker

        when ( markerDao.getMarkersByPlatformMarkerNameTuples( anyList())).thenReturn(
            mockMarkers
        );


        Exception exception = assertThrows(InvalidMarkersException.class, () -> {
            markerGroupServiceImpl.mapMarkers(123, inputMarkers, "test-user" );
        });

        assertTrue("Exception is null", exception != null);

        InvalidMarkersException exc = (InvalidMarkersException) exception;

        List<MarkerStatus> statusList = exc.getStatusList();
        assertTrue("Status list empty", statusList != null &&  statusList.size() == 1);
       
        
        MarkerStatus status = statusList.get(0);
        log.info(status.getError());
        assertTrue( status.getError().equals("Bad Request. Marker: KASP, test1 is invalid"));

    }
    
}