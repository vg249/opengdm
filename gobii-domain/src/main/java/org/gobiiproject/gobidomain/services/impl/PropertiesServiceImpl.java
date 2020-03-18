/**
 * PropertiesServiceImpl.java
 * 
 * Default PropertiesService implementation.
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-18
 */

package org.gobiiproject.gobidomain.services.impl;

import java.util.List;
import java.util.Objects;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.PropertiesService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.modelmapper.CvIdCvTermMapper;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesServiceImpl implements PropertiesService {

    @Autowired
    private CvDao cvDao;

    @Override
    public PagedResult<CvPropertyDTO> getProjectProperties(Integer page, Integer pageSize) throws Exception {
        PagedResult<CvPropertyDTO> pagedResult;

        try {
            Objects.requireNonNull(pageSize);
            Objects.requireNonNull(page);
            List<Cv> cvs = cvDao.getCvs(null, CvGroup.CVGROUP_PROJECT_PROP.getCvGroupName(), null, page, pageSize);
            List<CvPropertyDTO> converted = CvIdCvTermMapper.convert(cvs);
            pagedResult = new PagedResult<>();
            pagedResult.setResult(converted);
            pagedResult.setCurrentPageNum(page);
            pagedResult.setCurrentPageSize(converted.size());
            return pagedResult;
        } catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            log.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
    }

}