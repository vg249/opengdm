package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.entity.MarkerGroup;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.MarkerDao;
import org.gobiiproject.gobiisampletrackingdao.MarkerGroupDao;
import org.springframework.beans.factory.annotation.Autowired;

public class MarkerGroupServiceImpl implements MarkerGroupService {


    @Autowired
    private CvDao cvDao;

    @Autowired
    private MarkerGroupDao markerGroupDao;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private MarkerDao markerDao;

    @Transactional
    @Override
    public MarkerGroupDTO createMarkerGroup(MarkerGroupDTO request, String createdBy) throws Exception {

        MarkerGroup markerGroup = new MarkerGroup();
        markerGroup.setName(request.getMarkerGroupName());
        markerGroup.setGermplasmGroup(request.getGermplasmGroup());

        //code ::
        // TODO Auto-generated method stub
        Cv newStatus = cvDao.getNewStatus();
        markerGroup.setStatus(newStatus);

        //audit items
        Contact creator = contactDao.getContactByUsername(createdBy);
		if (creator != null)
			markerGroup.setCreatedBy(creator.getContactId());
        markerGroup.setCreatedDate(new java.util.Date());
        
        markerGroup = markerGroupDao.createMarkerGroup(markerGroup);
        MarkerGroupDTO createdMarkerGroupDTO = new MarkerGroupDTO();
        ModelMapper.mapEntityToDto(markerGroup, createdMarkerGroupDTO);

        return createdMarkerGroupDTO;


    }

    @Transactional
	@Override
	public PagedResult<MarkerGroupDTO> getMarkerGroups(Integer page, Integer pageSize) {
        Integer offset = page * pageSize;
        List<MarkerGroup> markerGroups = markerGroupDao.getMarkerGroups(offset, pageSize);
        List<MarkerGroupDTO> markerGroupDTOs = new ArrayList<>();

        markerGroups.forEach(markerGroup -> {
            MarkerGroupDTO markerGroupDTO = new MarkerGroupDTO();
            ModelMapper.mapEntityToDto(markerGroup, markerGroupDTO);
            markerGroupDTOs.add(markerGroupDTO);
        });

		return PagedResult.createFrom(page, markerGroupDTOs);
	}

    @Transactional
	@Override
	public MarkerGroupDTO getMarkerGroup(Integer markerGroupId) throws Exception {
        MarkerGroup markerGroup = this.loadMarkerGroup(markerGroupId);
        MarkerGroupDTO markerGroupDTO = new MarkerGroupDTO();
        ModelMapper.mapEntityToDto(markerGroup, markerGroupDTO);
        return markerGroupDTO;
	}

    @Transactional
	@Override
	public MarkerGroupDTO updateMarkerGroup(Integer markerGroupId, MarkerGroupDTO request, String updatedBy) throws Exception {
        MarkerGroup markerGroup = this.loadMarkerGroup(markerGroupId);
        boolean modified = false;
        if (!LineUtils.isNullOrEmpty(request.getMarkerGroupName())) {
            markerGroup.setName(request.getMarkerGroupName());
            modified = true;
        }
        if (!LineUtils.isNullOrEmpty(request.getGermplasmGroup())) {
            markerGroup.setGermplasmGroup(request.getGermplasmGroup());
            modified = true;
        }

        if (modified) {
            Contact editor = contactDao.getContactByUsername(updatedBy);
            if (editor != null)
                markerGroup.setModifiedBy(editor.getContactId());
            markerGroup.setModifiedDate(new java.util.Date());

            //status
            Cv modifiedStat = cvDao.getModifiedStatus();
            markerGroup.setStatus(modifiedStat);

            markerGroup = markerGroupDao.updateMarkerGroup(markerGroup);
        }

        MarkerGroupDTO markerGroupDTO = new MarkerGroupDTO();
        ModelMapper.mapEntityToDto(markerGroup, markerGroupDTO);
		return markerGroupDTO;
    }
    
    private MarkerGroup loadMarkerGroup(Integer markerGroupId) throws Exception {
        MarkerGroup markerGroup = markerGroupDao.getMarkerGroup(markerGroupId);
        if (markerGroup == null) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                "Not found");
        }
        return markerGroup;
    }

    @Transactional
	@Override
	public void deleteMarkerGroup(Integer markerGroupId) throws Exception {
        MarkerGroup markerGroup = this.loadMarkerGroup(markerGroupId);
        markerGroupDao.deleteMarkerGroup(markerGroup);
	}

    @Transactional
	@Override
	public PagedResult<MarkerDTO> mapMarkers(Integer markerGroupId, List<MarkerDTO> markers, String editedBy) throws Exception {
        Objects.requireNonNull(markers, "No markers to process");
        //check data size
        if (markers.size() > 1000) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                "Maximum number of allowed markers in list for a single request is 1000");
        }
        
        MarkerGroup markerGroup = this.loadMarkerGroup(markerGroupId);

        //get platform_name, marker name combo from list
        List<List<String>> markerTuples = new java.util.ArrayList<>();
        markers.forEach( markerDTO -> {
            List<String> tuple = new ArrayList<>();
            tuple.add(markerDTO.getPlatformName());
            tuple.add(markerDTO.getMarkerName());
            markerTuples.add(tuple);
        });

        List<Marker> existingMarkers = markerDao.getMarkersByPlatformMarkerNameTuples(markerTuples);

        if (existingMarkers.size() != markerTuples.size()) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                "Specified markers not found");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode currentNode = (ObjectNode) markerGroup.getMarkers();
        if (currentNode == null)  {
            currentNode = objectMapper.createObjectNode();
        }
        Integer counter = 0;
        List<MarkerDTO> markerDTOs = new ArrayList<>();
        for (Marker marker: existingMarkers) {
            ArrayNode arrayNode = currentNode.putArray(marker.getMarkerId().toString());
            String[] favorableAlleles = markers.get(counter).getFavorableAlleles();
            for (String allele: favorableAlleles) {
                arrayNode.add(allele);
            }
            counter++;

            //map to DTO
            MarkerDTO markerDTO = new MarkerDTO();
            ModelMapper.mapEntityToDto(marker, markerDTO);
            //set the favorableAllele
            markerDTO.setFavorableAlleles(favorableAlleles);
            markerDTOs.add(markerDTO);
        }

        markerGroup.setMarkers(currentNode);
        markerGroup = markerGroupDao.updateMarkerGroup(markerGroup);

		return PagedResult.createFrom(0, markerDTOs);
	}
    
}