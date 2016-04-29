package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.CvService;
import org.gobiiproject.gobidomain.services.DisplayService;
import org.gobiiproject.gobidomain.services.NameIdListService;
import org.gobiiproject.gobiidtomapping.DtoMapCv;
import org.gobiiproject.gobiidtomapping.DtoMapDisplay;
import org.gobiiproject.gobiidtomapping.DtoMapNameIdList;
import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Angel on 4/29/2016.
 */
public class CvServiceImpl implements CvService {

    @Autowired
    DtoMapCv dtoMapCv = null;


	@Override
	public CvDTO getCvNames(CvDTO cvDTO) {
		// TODO Auto-generated method stub
		return null;
	}
}
