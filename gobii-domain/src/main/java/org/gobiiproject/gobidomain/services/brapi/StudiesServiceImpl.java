package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.brapi.StudiesDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.gdmv3.ExperimentV3;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudiesServiceImpl implements StudiesService {

    Logger LOGGER = LoggerFactory.getLogger(StudiesServiceImpl.class);

    @Autowired
    private ExperimentDao experimentDao;

    @Override
    public PagedResult<StudiesDTO> getStudies(Integer pageSize, Integer pageNum,
                                              Integer projectId)
            throws GobiiDomainException {

        PagedResult<StudiesDTO> pagedResult = new PagedResult<>();

        try {

            Objects.requireNonNull(pageSize);
            Objects.requireNonNull(pageNum);

            List<StudiesDTO> studies = new ArrayList<>();

            Integer rowOffset = pageNum*pageSize;

            List<ExperimentV3> experiments = experimentDao.getExperiments(
                    pageSize, rowOffset, null);

            for (ExperimentV3 experiment : experiments) {


                StudiesDTO study = new StudiesDTO();

                ModelMapper.mapEntityToDto(experiment, study);

                studies.add(study);


            }

            pagedResult.setResult(studies);

            pagedResult.setCurrentPageNum(pageNum);

            pagedResult.setCurrentPageSize(pageSize);

            return pagedResult;

        } catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

    }

}
