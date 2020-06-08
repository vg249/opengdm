package org.gobiiproject.gobidomain.services.gdmv3;

import javax.transaction.Transactional;

import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.MarkerGroup;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.MarkerGroupDao;
import org.springframework.beans.factory.annotation.Autowired;

public class MarkerGroupServiceImpl implements MarkerGroupService {


    @Autowired
    private CvDao cvDao;

    @Autowired
    private MarkerGroupDao markerGroupDao;

    @Autowired
    private ContactDao contactDao;

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
    
}