package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.CvDTO;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.CvGroup;
import org.gobiiproject.gobiimodel.modelmapper.CvMapper;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.springframework.beans.factory.annotation.Autowired;

public class CvServiceImpl implements CvService {

    @Autowired
    private CvDao cvDao;

    @Transactional
    @Override
    public CvDTO createCv(CvDTO request) throws Exception {
        Cv cv = new Cv();
        //check the properties first if the propertyId exists and it is a property
        if (request.getProperties() != null && request.getProperties().size() > 0) {
            Map<String, String> propsMap = new HashMap<>();
            for (int i=0; i<request.getProperties().size(); i++) {
                CvPropertyDTO cvPropDTO = request.getProperties().get(i);
                Cv propCv = cvDao.getCvByCvId(cvPropDTO.getPropertyId());
                if (propCv == null) {
                    throw new GobiiDomainException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST, 
                        "Invalid property Id"
                    );
                }
                //TODO: check correct type?
                propsMap.put(cvPropDTO.getPropertyId().toString(), cvPropDTO.getPropertyValue());

            } 
            cv.setProperties(propsMap);
        }
    

        //check cvGroup Id
        CvGroup cvGroup = cvDao.getCvGroupById(request.getCvGroupId());
        if (cvGroup == null) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Invalid cv group id"
            );
        }
        //check correct cv group type?
        cv.setCvGroup(cvGroup);

        cv.setTerm(request.getCvName());
        cv.setDefinition(request.getCvDescription());
        
        //TODO:  no auditing on Cv?
        cv.setStatus(cvDao.getNewStatus().getCvId());

        cvDao.createCv(cv);

        CvDTO resultDTO = new CvDTO();
        ModelMapper.mapEntityToDto(cv, resultDTO);

        return resultDTO;
    }
    
}