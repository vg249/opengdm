package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.CvDTO;

public interface CvService {
	CvDTO createCv(CvDTO request) throws Exception;

	CvDTO updateCv(Integer id, CvDTO request) throws Exception;
}