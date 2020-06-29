package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.CvDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.CvGroupDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface CvService {
	CvDTO createCv(CvDTO request) throws Exception;

	CvDTO updateCv(Integer id, CvDTO request) throws Exception;

	PagedResult<CvDTO> getCvs(Integer page, Integer pageSize, String cvGroupName, String cvGroupType) throws Exception;

	CvDTO getCv(Integer id) throws Exception;

	PagedResult<CvPropertyDTO> getCvProperties(Integer page, Integer pageSize);

	CvPropertyDTO addCvProperty(CvPropertyDTO request) throws Exception;

	void deleteCv(Integer id) throws Exception;

	PagedResult<CvGroupDTO> getCvGroups(Integer page, Integer pageSize);
}