/**
 * ExperimentService.java
 * 
 * GDM V3 Experiment Service
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-28
 */

 package org.gobiiproject.gobidomain.services.gdmv3;

import javax.validation.Valid;

import org.gobiiproject.gobiimodel.dto.gdmv3.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.request.ExperimentRequest;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface ExperimentService {

	PagedResult<ExperimentDTO> getExperiments(Integer page, Integer pageSize, Integer projectId);

	ExperimentDTO getExperiment(Integer i) throws Exception;

	ExperimentDTO createExperiment(ExperimentRequest experiment, String userName) throws Exception;

 }